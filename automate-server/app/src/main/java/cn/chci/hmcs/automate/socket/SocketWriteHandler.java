package cn.chci.hmcs.automate.socket;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

/**
 * 负责处理socket输出数据，其中涉及到管道流的读取，该过程是导致阻塞，因此该类须要在单独的线程中运行。
 * 该类通过管道与负责socket读取数据的类进行交互，该类一直阻塞去读取管道中的数据，当负责socket读取数据的类
 * 读取到相关内容（来自客户端的信息）时，会向管道内输入内容，然后该类就会输出内容到客户端，整个过程由管道作为介质进行触发
 **/
public class SocketWriteHandler implements Runnable {
    private static final String LOG_TAG = "SocketWriteHandler";
    private final Integer id;
    private final Socket socket;

    public SocketWriteHandler(Integer id, Socket socket) {
        this.id = id;
        this.socket = socket;
    }

    /**
     * 压缩
     *
     * @param raw 源文本字节数组
     * @return 压缩后的字节数组
     */
    static byte[] compress(byte[] raw) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
            gzip.write(raw);
            gzip.close();
            return baos.toByteArray();
        } catch (IOException e) {
            Log.e(LOG_TAG, "compress: " + Arrays.toString(raw), e);
        }
        return new byte[0];
    }

    @Override
    public void run() {
        Thread.currentThread().setName("SocketWriteHandler-" + id);
        try (PipedInputStream in = Server.PIPE_IN.get(id);
             OutputStream out = socket.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int len;
            // read方法是阻塞的，直到从管道中读取到数据或者连接断开
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
                // flush必须写在循环内部，因为上面的read方法是阻塞的，当处理最后一批数据时 buffer没有装满，write就不会自动触发flush，就可能导致数据传输延迟                 out.flush();
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
