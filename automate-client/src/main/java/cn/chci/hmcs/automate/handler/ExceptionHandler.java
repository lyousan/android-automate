package cn.chci.hmcs.automate.handler;

import cn.chci.hmcs.automate.dto.Request;

/**
 * @Author 有三
 * @Date 2022-08-01 16:43
 * @Description
 **/
@FunctionalInterface
public interface ExceptionHandler {
    void handle(Request request, Exception e) throws Exception;
}
