package cn.chci.hmcs.automator.core;

import cn.chci.hmcs.automator.socket.Client;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 有三
 * @Date 2022-07-28 12:06
 * @Description
 **/
public class ClientContextHolder {
    private ClientContextHolder() {

    }

    private static final Map<String, Client> MAP = new HashMap<>();

    public static void put(String id, Client client) {
        MAP.put(id, client);
    }

    public static Client get(String id) {
        return MAP.get(id);
    }
}
