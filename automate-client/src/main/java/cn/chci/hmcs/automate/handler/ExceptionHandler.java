package cn.chci.hmcs.automate.handler;

import cn.chci.hmcs.automate.dto.Request;
import cn.chci.hmcs.automate.dto.Response;
import cn.chci.hmcs.automate.socket.Client;

/**
 * @Author 有三
 * @Date 2022-08-01 16:43
 * @Description
 **/
@FunctionalInterface
public interface ExceptionHandler<T extends Response> {
    T handle(Client client, Request request, Exception e) throws Exception;
}
