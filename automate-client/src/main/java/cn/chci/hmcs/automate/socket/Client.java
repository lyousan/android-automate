package cn.chci.hmcs.automate.socket;

import cn.chci.hmcs.automate.accessibility.fn.AbstractCommand;
import cn.chci.hmcs.automate.accessibility.fn.Global;
import cn.chci.hmcs.automate.dto.Request;
import cn.chci.hmcs.automate.dto.Response;
import cn.chci.hmcs.automate.exception.AutomateException;
import cn.chci.hmcs.automate.exception.ClientException;
import cn.chci.hmcs.automate.model.Command;
import cn.chci.hmcs.automate.utils.AdbUtils;
import cn.chci.hmcs.automate.utils.NodeParser;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @Author 有三
 * @Date 2022-07-23 13:03
 * @Description
 **/
@Slf4j
public class Client {
    public static final String PACKAGE_NAME = "cn.chci.hmcs.automate";
    public static final Integer DEFAULT_PC_PORT = 33579;
    public static final Integer DEFAULT_ANDROID_PORT = 33579;
    @Getter
    final private Integer pcPort;
    @Getter
    final private Integer androidPort;
    @Getter
    final private String udid;
    /**
     * 缓存管道
     */
    final PipedInputStream pipedIn = new PipedInputStream();
    final PipedOutputStream pipedOut = new PipedOutputStream();
    final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(pipedOut));
    Socket socket = null;
    final ExecutorService executorService = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());

    public Client(String udid) {
        this(udid, DEFAULT_PC_PORT, DEFAULT_ANDROID_PORT);
    }

    public Client(String udid, Integer pcPort, Integer androidPort) {
        this.udid = udid;
        this.pcPort = pcPort;
        this.androidPort = androidPort;
    }

    @SneakyThrows
    public void connect(boolean stopExistServer) {
        if (stopExistServer) {
            AdbUtils.exec("adb -s " + udid + " shell am force-stop " + PACKAGE_NAME);
        }
        // 提权获取无障碍权限（有可能显示已开启，但是不起作用，安卓系统的bug，需要重启）
        AdbUtils.exec("adb -s " + udid + " shell pm grant " + PACKAGE_NAME + " android.permission.WRITE_SECURE_SETTINGS");
        // 端口转发
        AdbUtils.exec("adb -s " + udid + " forward tcp:" + pcPort + " tcp:" + androidPort + "");
        // 启动app
        AdbUtils.exec("adb -s " + udid + " shell am start " + PACKAGE_NAME + "/.MainActivity");
        // 需要等待app启动
        Thread.sleep(1000);
        socket = new Socket("127.0.0.1", pcPort);
        pipedOut.connect(pipedIn);
        log.info("the connection with Automate established pc:port:{} android:port:{}", pcPort, androidPort);
        // 开启两个独立的线程去处理读和写
        executorService.submit(new SocketReadHandler(this, socket), "AutomateReader-" + udid);
        executorService.submit(new SocketWriteHandler(this, socket), "AutomateWriter-" + udid);
    }

    public void emit(Request request) {
        try {
            String msg = JSON.toJSONString(request);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(pipedOut));
            writer.write(msg);
            writer.newLine();
            writer.flush();
            log.debug("send request: {}\n", JSON.toJSONString(request, JSONWriter.Feature.PrettyFormat));
        } catch (Exception e) {
            if (socket.isClosed()) {
                log.warn("socket of Automate closed");
                recycle();
            } else {
                log.error("Client#emit error:", e);
            }
        }
    }

    public void close() {
        Request request = new Request();
        request.setCommand(new Command("close", null, null, null));
        emit(request);
        recycle();
        log.warn("Automate connection closed");
    }

    public boolean isClosed() {
        if (socket == null || socket.isClosed()) return true;
        Global global = new Global();
        global.setTimeout(2L);
        try {
            return global.ping(this);
        } catch (AutomateException e) {
            return false;
        }
    }


    public void recycle() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                log.error("close Automate error:", e);
            }
        }
        executorService.shutdownNow();
        NodeParser.localReader.remove();
    }
}
