package cn.chci.hmcs.automator.core;

/**
 * @Author 有三
 * @Date 2022-07-28 14:52
 * @Description
 **/
public class ThreadLocalContextHolder {
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    public static void setCurrentClientId(String id) {
        THREAD_LOCAL.set(id);
    }

    public static String getCurrentClientId() {
        return THREAD_LOCAL.get();
    }

}
