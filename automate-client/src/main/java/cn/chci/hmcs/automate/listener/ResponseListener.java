package cn.chci.hmcs.automate.listener;

import cn.chci.hmcs.automate.dto.Response;

/**
 * @Author 有三
 * @Date 2022-07-28 14:17
 * @Description
 **/
public interface ResponseListener<T extends Response> {
    void onReceive(T t);
}
