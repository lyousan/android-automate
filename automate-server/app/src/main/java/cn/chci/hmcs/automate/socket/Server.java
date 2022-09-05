package cn.chci.hmcs.automate.socket;

import android.util.Log;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import cn.chci.hmcs.automate.dto.Request;
import cn.chci.hmcs.automate.dto.Response;
import cn.chci.hmcs.automate.accessibility.fn.Executor;

public class Server {
    private static final String LOG_TAG = "Server";
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    /**
     * 缓存socket连接
     */
    static final Map<Integer, Socket> CLIENT_CACHE = new HashMap<>();
    static final Map<Integer, Future<?>> READER_CACHE = new HashMap<>();
    static final Map<Integer, Future<?>> WRITER_CACHE = new HashMap<>();
    /**
     * 缓存管道
     */
    static final Map<Integer, PipedInputStream> PIPE_IN = new HashMap<>();
    static final Map<Integer, PipedOutputStream> PIPE_OUT = new HashMap<>();
    /**
     * 计数器，用来给socket分配id
     */
    private static final AtomicInteger count = new AtomicInteger(0);

    private Server() {
    }

    public static void start() {
        // 在单独的线程中启动，防止阻塞主线程
        EXECUTOR_SERVICE.submit(() -> {
            Thread.currentThread().setName("Server");
            ServerSocket serverSocket = new ServerSocket(33579);
            while (true) {
                Socket client = serverSocket.accept();
                int id = count.addAndGet(1);
                Log.d(LOG_TAG, "start: 连接到客户端[" + id + "]");
                // 这个socket缓存暂时还没用，后续可以单开一个线程定时去清理已经断开的socket
                CLIENT_CACHE.put(id, client);
                // 绑定上管道流，由于socket的读取与输出是两个单独的线程来操作的，所以这里通过管道这个中间介质来让这两个线程通讯
                PipedOutputStream pipedOutputStream = new PipedOutputStream();
                PipedInputStream pipedInputStream = new PipedInputStream();
                pipedOutputStream.connect(pipedInputStream);
                PIPE_OUT.put(id, pipedOutputStream);
                PIPE_IN.put(id, pipedInputStream);
                READER_CACHE.put(id, EXECUTOR_SERVICE.submit(new SocketReadHandler(id, client)));
                WRITER_CACHE.put(id, EXECUTOR_SERVICE.submit(new SocketWriteHandler(id, client)));
            }
        });
    }

    /**
     * 用于触发socket输出的方法
     *
     * @param id      socket的id
     * @param request 客户端发送的请求
     */
    static void emit(Integer id, Request request) {
        try {
            if (request.getCommand().getTargetName().equals("close")) {
                close(id);
                return;
            }
            // 执行Command
            Response response = Executor.execute(request.getCommand());
            response.setRequestId(request.getId());
            response.setRequestTimestamp(request.getTimestamp());
            // 序列化执行结果
            String msg = Response.convertToJson(response);
            byte[] compressed = SocketWriteHandler.compress(msg.getBytes(StandardCharsets.UTF_8));
            PipedOutputStream pipedOutputStream = PIPE_OUT.get(id);
            // 前八个字节表示内容的长度
            byte[] tmp = new byte[compressed.length + 8];
            String compressedSizeStr = String.valueOf(compressed.length);
            System.arraycopy(compressedSizeStr.getBytes(), 0, tmp, 0, compressedSizeStr.length());
            System.arraycopy(compressed, 0, tmp, 8, compressed.length);
            pipedOutputStream.write(tmp);
            pipedOutputStream.flush();
        } catch (Exception e) {
            if (CLIENT_CACHE.get(id).isClosed()) {
                close(id);
            } else {
                Log.e(LOG_TAG, "Server#emit error: ", e);
            }
        }
    }

    static void close(Integer id) {
        if (CLIENT_CACHE.containsKey(id)) {
            Log.w(LOG_TAG, "client[" + id + "] will been closed");
            CLIENT_CACHE.remove(id);
            READER_CACHE.remove(id).cancel(true);
            WRITER_CACHE.remove(id).cancel(true);
        } else {
            Log.w(LOG_TAG, "client[" + id + "] has been closed");
        }
    }
}
