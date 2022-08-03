package cn.chci.hmcs.automator.socket;

import cn.chci.hmcs.automator.dto.Request;
import cn.chci.hmcs.common.toolkit.utils.AdbUtils;
import com.alibaba.fastjson2.JSON;
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
    /**
     * 缓存管道
     */
    final PipedInputStream pipedIn = new PipedInputStream();
    final PipedOutputStream pipedOut = new PipedOutputStream();
    final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(pipedOut));

    public void start(String udid) throws IOException, InterruptedException {
        // 提权获取无障碍权限（有可能显示已开启，但是不起作用，安卓系统的bug，需要重启）
        AdbUtils.exec("adb -s " + udid + " shell pm grant cn.chci.hmcs.automator android.permission.WRITE_SECURE_SETTINGS");
        // 端口转发
        AdbUtils.exec("adb -s " + udid + " forward tcp:33579 tcp:33579");
        // 启动app
        AdbUtils.exec("adb -s " + udid + " shell am start cn.chci.hmcs.automator/.MainActivity");
        // 回退app
        AdbUtils.exec("adb -s " + udid + " shell input keyevent 4");
        Thread.sleep(3000);
        Socket server = new Socket("127.0.0.1", 33579);
        pipedOut.connect(pipedIn);
        log.info("连接已建立......");
        // 开启两个独立的线程去处理读和写
        new Thread(new SocketReadHandler(this, server), "ReadHandler").start();
        new Thread(new SocketWriteHandler(this, server), "WriteHandler").start();
    }

    public void emit(Request request) {
        try {
            String msg = JSON.toJSONString(request);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(pipedOut));
            writer.write(msg);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            log.error("Client#emit", e);
        }
    }
}
