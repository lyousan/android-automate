package cn.chci.hmcs.automator.fn;

import cn.chci.hmcs.automator.dto.Request;

/**
 * @Author 有三
 * @Date 2022-08-01 16:43
 * @Description
 **/
@FunctionalInterface
public interface ExceptionHandler {
    void handle(Request request, Exception e) throws Exception;
}
