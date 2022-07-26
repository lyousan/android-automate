package cn.chci.hmcs.automator.socket;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author 有三
 * @Date 2022-07-23 13:03
 * @Description
 **/
public class Client {

    public static void start(String udid) throws IOException, InterruptedException {
        // 提权获取无障碍权限（有可能显示已开启，但是不起作用，安卓系统的bug，需要重启）
        exec("adb -s " + udid + " shell pm grant cn.chci.hmcs.automator android.permission.WRITE_SECURE_SETTINGS");
        // 端口转发
        exec("adb -s " + udid + " forward tcp:33579 tcp:33579");
        // 启动app
        exec("adb -s " + udid + " shell am start cn.chci.hmcs.automator/.MainActivity");
        Thread.sleep(3000);
        Socket server = new Socket("127.0.0.1", 33579);
        System.out.println("连接已建立......");
        // 开启两个独立的线程去处理读和写
        new Thread(new SocketReadHandler(server), "ReadHandler").start();
        new Thread(new SocketWriteHandler(server), "WriteHandler").start();
    }

    /**
     * 执行cmd命令，linux平台需要修改: "cmd" ==> "sh", "/c" ==> "-c"
     *
     * @param command 命令
     * @return 执行结果，返回一个集合，一行结果为一个元素，方便处理
     * @throws IOException
     * @throws InterruptedException
     */
    private static List<String> exec(String command) throws IOException, InterruptedException {
        List<String> list = new ArrayList<>();
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor(5, TimeUnit.SECONDS);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String len = null;
        while ((len = reader.readLine()) != null) {
            list.add(len);
        }
        reader.close();
        return list;
    }
}
