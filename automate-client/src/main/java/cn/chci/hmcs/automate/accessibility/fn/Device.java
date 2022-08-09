package cn.chci.hmcs.automate.accessibility.fn;

import cn.chci.hmcs.automate.dto.Request;
import cn.chci.hmcs.automate.dto.Response;
import cn.chci.hmcs.automate.model.Command;
import cn.chci.hmcs.automate.model.Rect;
import cn.chci.hmcs.automate.socket.Client;
import com.alibaba.fastjson2.JSON;

import java.awt.*;

/**
 * @author 有三
 * @date 2022-08-09 20:09
 * @description
 **/
public class Device extends AbstractCommand<Response> {
    public Rect getScreenSize(Client client) {
        Request request = new Request();
        Command command = new Command("Device", "getScreenSize", new Class[0], new Object[0]);
        request.setCommand(command);
        Response response = send(client, request);
        return JSON.parseObject(response.getData().toString(), Rect.class);
    }
}
