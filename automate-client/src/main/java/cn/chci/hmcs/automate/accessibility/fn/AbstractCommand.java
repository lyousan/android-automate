package cn.chci.hmcs.automate.accessibility.fn;

import cn.chci.hmcs.automate.dto.Request;
import cn.chci.hmcs.automate.dto.Response;
import cn.chci.hmcs.automate.exception.AutomatorException;
import cn.chci.hmcs.automate.exception.ClientException;
import cn.chci.hmcs.automate.exception.ServerException;
import cn.chci.hmcs.automate.exception.TimeoutException;
import cn.chci.hmcs.automate.handler.ExceptionHandler;
import cn.chci.hmcs.automate.handler.ResponseHandler;
import cn.chci.hmcs.automate.listener.ResponseListener;
import cn.chci.hmcs.automate.listener.ResponseListenerContextHolder;
import cn.chci.hmcs.automate.socket.Client;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
    protected CountDownLatch cdl = new CountDownLatch(1);
    protected final Map<String, T> RESULT = new HashMap<>();
    protected final ExceptionHandler DEFAULT_EXCEPTION_HANDLER = this::defaultOnException;
    protected final ResponseHandler DEFAULT_RESPONSE_HANDLER = this::defaultOnResponse;

    public AbstractCommand() {
        Field field = null;
        try {
            Class<? extends AbstractCommand> clazz = this.getClass();
            field = clazz.getDeclaredField("log");
            field.setAccessible(true);
            logger = (Logger) field.get(clazz);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger = log;
        }
    }

    public T send(Client client, Request request) {
        return send(client, request, DEFAULT_RESPONSE_HANDLER, DEFAULT_EXCEPTION_HANDLER);
    }

    public synchronized T send(Client client, Request request, ResponseHandler responseHandler, ExceptionHandler exceptionHandler) {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
        try {
            // 注册响应监听器
            ResponseListenerContextHolder.register(request.getId(), this);
            // 发送请求
            client.emit(request);
            // 等待异步响应
            if (!cdl.await(DEFAULT_TIMEOUT, DEFAULT_TIMEOUT_UNIT)) {
                // 超时
                throw new TimeoutException("响应超时");
            }
            // 返回响应
            T response = RESULT.get(request.getId());
            responseHandler.handle(request, response);
            return response;
        } catch (Exception e) {
            // 异常处理
            try {
                exceptionHandler.handle(request, e);
            } catch (Exception ex) {
                throw new AutomatorException(ex);
            }
        } finally {
            // 注销监听，移除本次响应，重置cdl（不重置的话下一次请求就不会阻塞等待响应了，会导致响应为null）
            ResponseListenerContextHolder.remove(request.getId());
            RESULT.remove(request.getId());
            cdl = new CountDownLatch(1);
        }
        return null;
    }


    protected void defaultOnResponse(Request request, Response response) {
        if (response == null) {
            logger.error("服务端返回为null");
            return;
        }
        String code = String.valueOf(response.getCode());
        if (code.startsWith("4")) {
            throw new ClientException(response.getMsg() + "\ndetails: " + response.getData().toString());
        } else if (code.startsWith("5")) {
            throw new ServerException(response.getMsg() + "\ndetails: " + response.getData().toString());
        }
    }

    protected void defaultOnException(Request request, Exception e) throws Exception {
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
        cdl.countDown();
    }

}
