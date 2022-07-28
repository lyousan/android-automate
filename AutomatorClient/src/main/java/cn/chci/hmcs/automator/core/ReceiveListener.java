package cn.chci.hmcs.automator.core;

/**
 * @Author 有三
 * @Date 2022-07-28 14:17
 * @Description
 **/
public interface ReceiveListener<T> {
    void onReceive(T t);
}
