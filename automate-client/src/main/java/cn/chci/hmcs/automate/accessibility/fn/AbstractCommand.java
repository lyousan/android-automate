package cn.chci.hmcs.automate.accessibility.fn;

import cn.chci.hmcs.automate.dto.Request;
import cn.chci.hmcs.automate.dto.Response;
import cn.chci.hmcs.automate.exception.AutomateClosedException;
import cn.chci.hmcs.automate.exception.TimeoutException;
import cn.chci.hmcs.automate.handler.ExceptionHandler;
import cn.chci.hmcs.automate.handler.ResponseHandler;
import cn.chci.hmcs.automate.listener.ResponseListener;
import cn.chci.hmcs.automate.listener.ResponseListenerContextHolder;
import cn.chci.hmcs.automate.socket.Client;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author 有三
 * @Date 2022-07-28 22:11
 * @Description
 **/
@Slf4j
public abstract class AbstractCommand<T extends Response> implements ResponseListener<T> {
    protected Logger logger;
    protected static final Long DEFAULT_TIMEOUT = 30L;
    protected static final TimeUnit DEFAULT_TIMEOUT_UNIT = TimeUnit.SECONDS;
    protected Long timeout = DEFAULT_TIMEOUT;
    protected TimeUnit timeoutUnit = DEFAULT_TIMEOUT_UNIT;
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    protected final Map<String, T> RESULT = new HashMap<>();
    protected final ExceptionHandler<T> DEFAULT_EXCEPTION_HANDLER = this::defaultOnException;
    protected final ResponseHandler<T> DEFAULT_RESPONSE_HANDLER = this::defaultOnResponse;
    protected int reconnectedCount = 0;

    public AbstractCommand() {
        try {
            Class<? extends AbstractCommand> clazz = this.getClass();
            Field field = clazz.getDeclaredField("log");
            field.setAccessible(true);
            logger = (Logger) field.get(clazz);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger = log;
        }
    }

    protected void sendPreprocess(Request request) {
        request.setTimestamp(System.currentTimeMillis());
    }

    public T send(Client client, Request request) {
        return send(client, request, DEFAULT_RESPONSE_HANDLER, DEFAULT_EXCEPTION_HANDLER);
    }

    @SneakyThrows
    public synchronized T send(Client client, Request request, ResponseHandler<T> responseHandler, ExceptionHandler<T> exceptionHandler) {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
        T response = null;
        try {
            // 注册响应监听器
            ResponseListenerContextHolder.register(request.getId(), this);
            // 前置处理request
            sendPreprocess(request);
            // 发送请求
            client.emit(request);
            // 等待异步响应
            if (!countDownLatch.await(timeout, timeoutUnit)) {
                if (client.isClosed()) {
                    throw new AutomateClosedException("Automate连接已断开");
                }
                // 超时
                throw new TimeoutException("响应超时");
            }
            // 返回响应
            response = RESULT.get(request.getId());
            response = responseHandler.handle(request, response);
            reconnectedCount = 0;
        } catch (Exception e) {
            // 异常处理
            response = exceptionHandler.handle(client, request, e);
        } finally {
            // 注销监听，移除本次响应，重置cdl（不重置的话下一次请求就不会阻塞等待响应了，会导致响应为null）
            ResponseListenerContextHolder.remove(request.getId());
            RESULT.remove(request.getId());
            countDownLatch = new CountDownLatch(1);
        }
        return response;
    }

    protected T defaultOnResponse(Request request, T response) throws Exception {
        if (response == null) {
            logger.error("服务端返回为null");
            return null;
        }
        String code = String.valueOf(response.getCode());
        // 包装原始异常
        if (!"200".equals(code)) {
            try {
                String[] split = response.getData().toString().split("\n");
                String clazzName = split.length > 0 ? split[0].substring(0, split[0].contains(":") ? split[0].indexOf(":") : split[0].length()) : "";
                Class<?> clazz = Class.forName(clazzName);
                Constructor<Exception> constructor = (Constructor<Exception>) clazz.getConstructor(String.class);
                throw constructor.newInstance(response.getData().toString());
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException | InvocationTargetException e) {
                logger.error("处理异常时发生异常 ==> ", e);
            }
        }
        return response;
    }

    protected T defaultOnException(Client client, Request request, Exception e) throws Exception {
        if (e instanceof InterruptedException) {
            return null;
        }
        // 断线重连
        if (e instanceof AutomateClosedException || e instanceof ConnectException) {
            if (reconnectedCount++ < client.reconnectCount) {
                logger.warn("Automate连接异常，等待重试[{}]", reconnectedCount);
                TimeUnit.SECONDS.sleep(5);
                try {
                    client.close();
                    client = new Client(client.getUdid());
                    client.connect(false);
                } catch (Exception ignore) {
                }
                countDownLatch = new CountDownLatch(1);
                if ("ping".equals(request.getCommand().getCommandName())) {
                    return null;
                }
                return send(client, request);
            }
        }
        logger.error("请求[ " + request.getId() + " ]发生异常", e);
        throw e;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public TimeUnit getTimeoutUnit() {
        return timeoutUnit;
    }

    public void setTimeoutUnit(TimeUnit timeoutUnit) {
        this.timeoutUnit = timeoutUnit;
    }

    @Override
    public void onReceive(T t) {
        RESULT.put(t.getRequestId(), t);
        countDownLatch.countDown();
    }

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("old String");
        foo(list.get(0));
        System.out.println(list.get(0));
    }

    private static void foo(String string) {
        string = new String("new String");
    }


}
