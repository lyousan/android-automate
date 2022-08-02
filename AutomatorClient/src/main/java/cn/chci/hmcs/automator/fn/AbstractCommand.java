package cn.chci.hmcs.automator.fn;

import cn.chci.hmcs.automator.core.ClientContextHolder;
import cn.chci.hmcs.automator.core.ResponseListener;
import cn.chci.hmcs.automator.core.ResponseListenerContextHolder;
import cn.chci.hmcs.automator.core.ThreadLocalContextHolder;
import cn.chci.hmcs.automator.dto.Request;
import cn.chci.hmcs.automator.dto.Response;
import cn.chci.hmcs.automator.exception.ClientException;
import cn.chci.hmcs.automator.exception.ServerException;
import cn.chci.hmcs.automator.socket.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Author 有三
 * @Date 2022-07-28 22:11
 * @Description
 **/
public abstract class AbstractCommand<T extends Response> implements ResponseListener<T> {
    protected static final Long DEFAULT_TIMEOUT = 300000L;
    protected static final TimeUnit DEFAULT_TIMEOUT_UNIT = TimeUnit.SECONDS;
    protected Long timeout = DEFAULT_TIMEOUT;
    protected TimeUnit timeoutUnit = DEFAULT_TIMEOUT_UNIT;
    protected final CountDownLatch CDL = new CountDownLatch(1);
    protected final Map<String, T> RESULT = new HashMap<>();
    protected final ExceptionHandler DEFAULT_EXCEPTION_HANDLER = this::defaultOnException;
    protected final ResponseHandler DEFAULT_RESPONSE_HANDLER = this::defaultOnResponse;

    public T send(Request request) {
        return send(request, DEFAULT_RESPONSE_HANDLER, DEFAULT_EXCEPTION_HANDLER);
    }

    public T send(Request request, ResponseHandler responseHandler, ExceptionHandler exceptionHandler) {
        Client client = ClientContextHolder.get(ThreadLocalContextHolder.getCurrentClientId());
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
        try {
            // 注册响应监听器
            ResponseListenerContextHolder.register(request.getId(), this);
            // 发送请求
            client.emit(request);
            // 等待异步响应
            if (!CDL.await(DEFAULT_TIMEOUT, DEFAULT_TIMEOUT_UNIT)) {
                // 超时
                throw new TimeoutException("响应超时");
            }
            // 返回响应
            T response = RESULT.get(request.getId());
            responseHandler.handle(response);
            return response;
        } catch (Exception e) {
            // 异常处理
            try {
                exceptionHandler.handle(request, e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            // 注销监听，移除本次响应
            ResponseListenerContextHolder.remove(request.getId());
            RESULT.remove(request.getId());
        }
        return null;
    }


    protected void defaultOnResponse(Response response) {
        String code = String.valueOf(response.getCode());
        if (code.startsWith("4")) {
            throw new ClientException(response.getMsg() + "\ndetails: " + response.getData().toString());
        } else if (code.startsWith("5")) {
            throw new ServerException(response.getMsg() + "\ndetails: " + response.getData().toString());
        }
    }

    protected void defaultOnException(Request request, Exception e) throws Exception {
        System.out.println("请求" + request.getId() + "发生异常");
        e.printStackTrace();
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
        CDL.countDown();
    }

}
