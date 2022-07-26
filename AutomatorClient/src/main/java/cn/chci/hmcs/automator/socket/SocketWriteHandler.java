package cn.chci.hmcs.automator.socket;

import java.io.*;
import java.net.Socket;

/**
 * @Author 有三
 * @Date 2022-07-23 14:19
 * @Description
 **/
public class SocketWriteHandler implements Runnable {
    private final Socket socket;

    public SocketWriteHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            while (true) {
                String msg = reader.readLine();
                writer.write(msg);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
