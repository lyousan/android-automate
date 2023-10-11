package cn.chci.hmcs.automate.socket;

import cn.chci.hmcs.automate.accessibility.fn.Global;
import cn.chci.hmcs.automate.dto.Request;
import cn.chci.hmcs.automate.exception.TimeoutException;
import cn.chci.hmcs.automate.model.Command;
import cn.chci.hmcs.automate.utils.AdbUtils;
import cn.chci.hmcs.automate.utils.NodeParser;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    private Integer pcPort;
    @Getter
    private Integer androidPort;
    @Getter
    final private String udid;
    public int reconnectCount = DEFAULT_RECONNECT_COUNT;
    public static final int DEFAULT_RECONNECT_COUNT = 3;
    /**
     * 缓存管道
     */
    PipedInputStream pipedIn;
    PipedOutputStream pipedOut;
    BufferedWriter writer;
    Socket socket;
    ExecutorService executorService;

    public Client(String udid) {
        this(udid, DEFAULT_PC_PORT, DEFAULT_ANDROID_PORT);
    }

    public Client(String udid, Integer pcPort, Integer androidPort) {
        this.udid = udid;
        this.pcPort = pcPort;
        this.androidPort = androidPort;
    }

    @SneakyThrows
    private void readyResource() {
        socket = new Socket("127.0.0.1", pcPort);
        pipedIn = new PipedInputStream();
        pipedOut = new PipedOutputStream();
        pipedOut.connect(pipedIn);
        writer = new BufferedWriter(new OutputStreamWriter(pipedOut));
        executorService = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
    }

    @SneakyThrows
    public void connect(boolean stopExistServer) {
        if (stopExistServer) {
            AdbUtils.exec("adb -s " + udid + " shell am force-stop " + PACKAGE_NAME);
        }
        pcPort = obtainPort(pcPort);
        // 提权获取无障碍权限（有可能显示已开启，但是不起作用，安卓系统的bug，需要重启）
        AdbUtils.exec("adb -s " + udid + " shell pm grant " + PACKAGE_NAME + " android.permission.WRITE_SECURE_SETTINGS");
        // 端口转发
        AdbUtils.exec("adb -s " + udid + " forward tcp:" + pcPort + " tcp:" + androidPort);
        // 启动app
        AdbUtils.exec("adb -s " + udid + " shell am start " + PACKAGE_NAME + "/.MainActivity");
        // 需要等待app启动
        Thread.sleep(1000);
        readyResource();
        log.info("the connection with Automate established pc:port:{} android:port:{}", pcPort, androidPort);
        // 开启两个独立的线程去处理读和写
        executorService.submit(new SocketReadHandler(this, socket), "AutomateReader-" + udid);
        executorService.submit(new SocketWriteHandler(this, socket), "AutomateWriter-" + udid);
    }

    public void emit(Request request) {
        try {
            String msg = JSON.toJSONString(request);
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(pipedOut));
            writer.write(msg);
            writer.newLine();
            writer.flush();
            log.debug("send request: {}\n", JSON.toJSONString(request, JSONWriter.Feature.PrettyFormat));
        } catch (Exception e) {
            if (socket == null || socket.isClosed()) {
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
        global.setTimeout(5L);
        try {
            return !global.ping(this);
        } catch (TimeoutException e) {
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
        if (executorService != null) {
            executorService.shutdownNow();
            executorService = null;
        }
        try {
            pipedIn.close();
            pipedIn = null;
        } catch (Exception ignore) {
        }
        try {
            pipedOut.close();
            pipedOut = null;
        } catch (Exception ignore) {
        }
        try {
            writer.close();
            writer = null;
        } catch (Exception ignore) {
        }
        NodeParser.localReader.remove();
        pcPort += 1;
    }

    private int obtainPort(int port) {
        try (Socket socket = new Socket("127.0.0.1", port)) {
            // 如果能够成功连接到指定的主机和端口，则端口被占用
            InetSocketAddress inetSocketAddress = new InetSocketAddress(0);
            try (Socket temp = new Socket();) {
                temp.bind(inetSocketAddress);
                return temp.getLocalPort();
            } catch (Exception e) {
                return port;
            }
        } catch (Exception e) {
            // 如果连接发生异常，则端口未被占用
            return port;
        }
    }
}
