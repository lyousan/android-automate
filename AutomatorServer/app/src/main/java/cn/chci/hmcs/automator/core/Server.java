package cn.chci.hmcs.automator.core;

import android.util.Log;

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

import cn.chci.hmcs.automator.utils.BeanContextHolder;
import cn.chci.hmcs.automator.layout.LayoutInspector;
import cn.chci.hmcs.automator.layout.NodeInfo;
import cn.chci.hmcs.automator.layout.NodeInfoParser;

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
    private static final Map<Integer, PipedInputStream> PIPE_IN = new HashMap<>();
    private static final Map<Integer, PipedOutputStream> PIPE_OUT = new HashMap<>();
    /**
     * 计数器，用来给socket分配id
     */
    private static final AtomicInteger count = new AtomicInteger(0);
    private static final BeanContextHolder beanContextHolder = BeanContextHolder.getInstance();

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
                EXECUTOR_SERVICE.submit(new ReadHandler(id, client));
                EXECUTOR_SERVICE.submit(new WriteHandler(id, client));
            }
        });
    }

    /**
     * 用于触发socket输出的方法
     *
     * @param id   socket的id
     * @param type TODO 后续作为指令进行不同的操作
     */
    private static void emit(Integer id, String type) {
        try {
            String msg = "很高兴收到你的消息: " + type;
            // TODO 目前瞎写的处理
            if (type.equalsIgnoreCase("dump")) {
                // 该类用于获取当前界面的节点信息
                LayoutInspector layoutInspector = ((LayoutInspector) beanContextHolder.getBean("layoutInspector"));
                if (layoutInspector != null) {
                    NodeInfo nodeInfo = layoutInspector.captureCurrentWindow();
                    // 将节点信息解析成xml
                    msg = NodeInfoParser.toXMLString(nodeInfo);
                } else {
                    msg = "layoutInspector暂未初始化";
                }
            }
            Log.d(LOG_TAG, "send msg: " + msg);
            byte[] compressed = WriteHandler.compress(msg.getBytes(StandardCharsets.UTF_8));
            PipedOutputStream pipedOutputStream = PIPE_OUT.get(id);
            // 前八个字节表示内容的长度
            byte[] tmp = new byte[compressed.length + 8];
            String compressedSizeStr = String.valueOf(compressed.length);
            System.arraycopy(compressedSizeStr.getBytes(), 0, tmp, 0, compressedSizeStr.length());
            System.arraycopy(compressed, 0, tmp, 8, compressed.length);
            pipedOutputStream.write(tmp);
            pipedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 负责处理socket读取，读取操作是阻塞的，该类须在单独的线程中运行，通过管道与负责处理socket输出的类进行交互，具体过程见{@link WriteHandler}类中的注释
     */
    private static class ReadHandler implements Runnable {
        private final Integer id;
        private final Socket socket;

        public ReadHandler(Integer id, Socket socket) {
            this.id = id;
            this.socket = socket;
        }

        @Override
        public void run() {
            Thread.currentThread().setName("ReadHandler-" + id);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String content = "";
                // readLine方法是阻塞的，会一直等到读取到数据或者连接关闭
                while ((content = reader.readLine()) != null) {
                    Log.d(LOG_TAG, "writeHandler received: " + content);
                    emit(id, content);
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "writeHandler error: ", e);
            }
        }
    }

    /**
     * 负责处理ocket输出数据，其中涉及到管道流的读取，该过程是导致阻塞，因此该类须要在单独的线程中运行。
     * 该类通过管道与负责socket读取数据的类进行交互，该类一直阻塞去读取管道中的数据，当负责socket读取数据的类
     * 读取到相关内容（来自客户端的信息）时，会向管道内输入内容，然后该类就会输出内容到客户端，整个过程由管道作为介质进行触发
     **/
    private static class WriteHandler implements Runnable {
        private final Integer id;
        private final Socket socket;

        public WriteHandler(Integer id, Socket socket) {
            this.id = id;
            this.socket = socket;
        }

        /**
         * 压缩
         *
         * @param raw 源文本字节数组
         * @return 压缩后的字节数组
         */
        public static byte[] compress(byte[] raw) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
                gzip.write(raw);
                gzip.close();
                return baos.toByteArray();
            } catch (IOException e) {
                Log.e(LOG_TAG, "compress: " + raw, e);
            }
            return new byte[0];
        }

        @Override
        public void run() {
            Thread.currentThread().setName("WriteHandler-" + id);
            try (PipedInputStream in = PIPE_IN.get(id);
                 OutputStream out = socket.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int len = 0;
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

}
