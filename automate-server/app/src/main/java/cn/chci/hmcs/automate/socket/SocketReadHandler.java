package cn.chci.hmcs.automate.socket;

import android.util.Log;
import cn.chci.hmcs.automate.dto.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 负责处理socket读取，读取操作是阻塞的，该类须在单独的线程中运行，通过管道与负责处理socket输出的类进行交互，具体过程见{@link SocketWriteHandler}类中的注释
 */
public class SocketReadHandler implements Runnable {
    private static final String LOG_TAG = "SocketReadHandler";
    private final Integer id;
    private final Socket socket;

    public SocketReadHandler(Integer id, Socket socket) {
        this.id = id;
        this.socket = socket;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("SocketReadHandler-" + id);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String content;
            // readLine方法是阻塞的，会一直等到读取到数据或者连接关闭
            while ((content = reader.readLine()) != null) {
                Log.d(LOG_TAG, "SocketReadHandler received: " + content);
                Request request = Request.convertToRequest(content);
                Server.emit(id, request);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "SocketReadHandler error: ", e);
        } finally {
            Server.close(id);
        }
    }
}
