package cn.chci.hmcs.automator.fn;

import cn.chci.hmcs.automator.dto.Response;

/**
 * @author 有三
 * @date 2022-08-02 20:41
 * @description
 **/
@FunctionalInterface
public interface ResponseHandler {
    void handle(Response response) throws Exception;
}
