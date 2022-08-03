package cn.chci.hmcs.automator.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

/**
 * @Author 有三
 * @Date 2022-07-23 14:19
 * @Description
 **/
@Slf4j
public class SocketWriteHandler implements Runnable {
    private final Client client;
    private final Socket socket;

    public SocketWriteHandler(Client client, Socket socket) {
        this.client = client;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.pipedIn));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            String msg = null;
            while (true) {
                msg = reader.readLine();
                writer.write(msg);
                writer.newLine();
                writer.flush();
            }
        } catch (Exception e) {
            log.error("socket写入线程发生异常", e);
        }
    }
}
