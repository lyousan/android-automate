package cn.chci.hmcs.automator.core;

import android.util.Log;

import org.dom4j.DocumentException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPOutputStream;

import cn.chci.hmcs.automator.layout.LayoutCache;
import cn.chci.hmcs.automator.utils.BeanContextHolder;
import cn.chci.hmcs.automator.layout.LayoutInspector;
import cn.chci.hmcs.automator.layout.NodeInfo;
import cn.chci.hmcs.automator.layout.NodeInfoParser;
import cn.chci.hmcs.automator.utils.LayoutInspectorGetter;

public class Server {
    private static final String LOG_TAG = "hmcs-automator";
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    /**
     * 缓存socket连接
     */
    private static final Map<Integer, Socket> CACHE = new HashMap<>();
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
                CACHE.put(id, client);
                // 绑定上管道流，由于socket的读取与输出是两个单独的线程来操作的，所以这里通过管道这个中间介质来让这两个线程通讯
                PipedOutputStream pipedOutputStream = new PipedOutputStream();
                PipedInputStream pipedInputStream = new PipedInputStream();
                pipedOutputStream.connect(pipedInputStream);
                PIPE_OUT.put(id, pipedOutputStream);
                PIPE_IN.put(id, pipedInputStream);
                EXECUTOR_SERVICE.submit(new SocketReadHandler(id, client));
                EXECUTOR_SERVICE.submit(new SocketWriteHandler(id, client));
            }
        });
    }

    /**
     * 用于触发socket输出的方法
     *
     * @param id   socket的id
     * @param type TODO 后续作为指令进行不同的操作
     */
    static void emit(Integer id, String type) {
        try {
            String msg = "很高兴收到你的消息: " + type;
            // TODO 目前瞎写的处理
            if (type.equalsIgnoreCase("dump")) {
                // 该类用于获取当前界面的节点信息
                LayoutInspector layoutInspector = LayoutInspectorGetter.getInstance();
                NodeInfo nodeInfo = layoutInspector.captureCurrentWindow();
                // 将节点信息解析成xml
                msg = NodeInfoParser.toXMLString(nodeInfo);
                LayoutCache.save(msg, nodeInfo);
            } else if (type.equalsIgnoreCase("find")) {
                NodeInfo one = LayoutCache.findOne(type);
                msg = NodeInfoParser.toXMLString(one);
            }
            Log.d(LOG_TAG, "send msg: " + msg);
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
            e.printStackTrace();
        }
    }

}
