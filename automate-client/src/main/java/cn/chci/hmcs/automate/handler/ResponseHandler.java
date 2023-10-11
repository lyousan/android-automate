package cn.chci.hmcs.automate.handler;

import cn.chci.hmcs.automate.dto.Request;
import cn.chci.hmcs.automate.dto.Response;

/**
 * @author 有三
 * @date 2022-08-02 20:41
 * @description
 **/
@FunctionalInterface
public interface ResponseHandler<T extends Response> {
    T handle(Request request, T response) throws Exception;
}
