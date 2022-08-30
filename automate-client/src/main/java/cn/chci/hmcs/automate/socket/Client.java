package cn.chci.hmcs.automate.socket;

import cn.chci.hmcs.automate.dto.Request;
import cn.chci.hmcs.automate.model.Command;
import cn.chci.hmcs.common.toolkit.utils.AdbUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

/**
 * @Author 有三
 * @Date 2022-07-23 13:03
 * @Description
 **/
@Slf4j
public class Client {
    public static final String PACKAGE_NAME = "cn.chci.hmcs.automate";
    /**
     * 缓存管道
     */
    final PipedInputStream pipedIn = new PipedInputStream();
    final PipedOutputStream pipedOut = new PipedOutputStream();
    final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(pipedOut));
    Socket socket = null;

    public void start(String udid) throws IOException, InterruptedException {
        // 关闭automate 如果要对app进行debug的话需要注释关闭automate的这一行
//        AdbUtils.exec("adb -s " + udid + " shell am force-stop " + PACKAGE_NAME);
        // 提权获取无障碍权限（有可能显示已开启，但是不起作用，安卓系统的bug，需要重启）
        AdbUtils.exec("adb -s " + udid + " shell pm grant " + PACKAGE_NAME + " android.permission.WRITE_SECURE_SETTINGS");
        // 端口转发
        AdbUtils.exec("adb -s " + udid + " forward tcp:33579 tcp:33579");
        // 启动app
        AdbUtils.exec("adb -s " + udid + " shell am start " + PACKAGE_NAME + "/.MainActivity");
        // 回退app
        AdbUtils.exec("adb -s " + udid + " shell input keyevent 4");
        socket = new Socket("127.0.0.1", 33579);
        pipedOut.connect(pipedIn);
        log.info("the connection with Automate established");
        // 开启两个独立的线程去处理读和写
        new Thread(new SocketReadHandler(this, socket), "ReadHandler-" + udid).start();
        new Thread(new SocketWriteHandler(this, socket), "WriteHandler-" + udid).start();
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
            } else {
                log.error("Client#emit error:", e);
            }
        }
    }

    public void close() {
        try {
            Request request = new Request();
            request.setCommand(new Command("close", null, null, null));
            emit(request);
            socket.close();
        } catch (IOException e) {
            log.error("close Automate error:", e);
        }
    }
}
