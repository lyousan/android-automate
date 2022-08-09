package cn.chci.hmcs.automate.listener;

import cn.chci.hmcs.automate.dto.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 有三
 * @Date 2022-07-28 14:18
 * @Description
 **/
public class ResponseListenerContextHolder {
    private final static Map<String, ResponseListener<Response>> LISTENERS = new HashMap<>();

    public static void trigger(String requestId, Response content) {
        if (LISTENERS.size() == 0) {
            return;
        }
        ResponseListener<Response> responseListener = LISTENERS.get(requestId);
        if (responseListener != null) {
            responseListener.onReceive(content);
        }
    }

    public static void trigger(Response content) {
        LISTENERS.forEach((s, listener) -> listener.onReceive(content));
    }

    public static void register(String requestId, ResponseListener listener) {
        LISTENERS.put(requestId, listener);
    }

    public static void remove(String requestId) {
        LISTENERS.remove(requestId);
    }

}
