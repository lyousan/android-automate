package cn.chci.hmcs.automate.socket;

import cn.chci.hmcs.automate.listener.ResponseListenerContextHolder;
import cn.chci.hmcs.automate.dto.Response;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.zip.GZIPInputStream;

/**
 * @Author 有三
 * @Date 2022-07-23 14:24
 * @Description
 **/
@Slf4j
public class SocketReadHandler implements Runnable {
    private final Socket socket;
    private final Client client;

    public SocketReadHandler(Client client, Socket socket) {
        this.client = client;
        this.socket = socket;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("automate-reader[" + client.getUdid() + "]");
        try (InputStream in = socket.getInputStream()) {
            byte[] buffer = new byte[8192];
            while (true) {
                if (in.available() == 0) {
//                    Thread.sleep(100);
                    continue;
                }
                // 每一次读取到字节数量
                int len = 0;
                // 存放当前这一次消息的所有字节，因为消息经过gzip压缩，需要整个字节数组来还原，所以下面会看到频繁的合并扩容该数组
                // gzip压缩如果使用字符传输的话需要注意编码格式，一定要使用ISO-8859-1这种单字节的字符编码，否则会解不出来，如果直接用字节数组来传输的话就不存在这个问题了
                byte[] decompressData = new byte[0];
                // 这个i是用来记录总共读取了多少字节，跟size做比较来判断数据是否接收完了
                // -8 是因为前8个字节是用来表示消息的长度
                int i = -8;
                // 消息的总长度，消息可能因为大小问题会被分成多次传输，这个size是指整个消息的总长度，通过第一批来的数据的前8个字节获取
                int size = 0;
                while (i < size && (len = in.read(buffer)) > 0) {
                    if (i == -8) {
                        // 这一步是在将前8个字节转换为数字，只有在处理第一批数据时才会做这个处理
                        int length = 0;
                        for (int j = 0; j < buffer.length; j++) {
                            if (buffer[j] == 0) {
                                length = j;
                                break;
                            }
                        }
                        size = Integer.parseInt(new String(buffer, 0, length));
                        // 重新拷贝一份不带前8个字节的字节数组，这个数组里才是真正的内容
                        byte[] tmp = new byte[len - 8];
                        System.arraycopy(buffer, 8, tmp, 0, len - 8);
                        decompressData = mergeArray(decompressData, tmp);
                    } else {
                        byte[] tmp = new byte[len];
                        System.arraycopy(buffer, 0, tmp, 0, len);
                        decompressData = mergeArray(decompressData, tmp);
                    }
                    i += len;
                }
                // 解压字节数组，转成字符串使用即可
                decompressData = decompress(decompressData);
                String msg = new String(decompressData);
                Response response = JSON.parseObject(msg, Response.class);
                msg = JSON.toJSONString(response, JSONWriter.Feature.PrettyFormat);
                log.debug("received response take {} ms \n{}", (response.getTimestamp() - response.getRequestTimestamp()), msg);
                ResponseListenerContextHolder.trigger(response.getRequestId(), response);
            }
        } catch (Exception e) {
            if (socket == null || socket.isClosed()) {
                log.warn("socket of Automate closed");
                client.recycle();
            } else {
                log.error("socketReader error:", e);
            }
        }
    }

    /**
     * 合并字节数组
     *
     * @param arr0 字节数组
     * @param arr1 字节数组
     * @return 合并后的字节数组（arr0+arr1）
     */
    private static byte[] mergeArray(byte[] arr0, byte[] arr1) {
        byte[] result = new byte[arr0.length + arr1.length];
        System.arraycopy(arr0, 0, result, 0, arr0.length);
        System.arraycopy(arr1, 0, result, arr0.length, arr1.length);
        return result;
    }

    /**
     * 解压gzip数据
     *
     * @param compressed gzip压缩过的字节数组
     * @return 解压后端字节数组
     */
    private static byte[] decompress(byte[] compressed) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream in = new ByteArrayInputStream(compressed);
             GZIPInputStream gzip = new GZIPInputStream(in)) {
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = gzip.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
