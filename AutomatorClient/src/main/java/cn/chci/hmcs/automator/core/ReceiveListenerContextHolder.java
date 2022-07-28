package cn.chci.hmcs.automator.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 有三
 * @Date 2022-07-28 14:18
 * @Description
 **/
public class ReceiveListenerContextHolder {
    private final static Map<String, ReceiveListener> LISTENERS = new HashMap<>();

    public static void trigger(String requestId, Object content) {
        if (LISTENERS.size() == 0) {
            return;
        }
        LISTENERS.get(requestId).onReceive(content);
    }

    public static void trigger(Object content) {
        LISTENERS.forEach((s, listener) -> listener.onReceive(content));
    }

    public static void register(String requestId, ReceiveListener listener) {
        LISTENERS.put(requestId, listener);
    }

    public static void remove(String requestId) {
        LISTENERS.remove(requestId);
    }

}
