package cn.chci.hmcs.automate.accessibility.fn;

import cn.chci.hmcs.automate.dto.Request;
import cn.chci.hmcs.automate.dto.Response;
import cn.chci.hmcs.automate.model.Command;
import cn.chci.hmcs.automate.socket.Client;

/**
 * @author 有三
 * @date 2022-08-08 22:15
 * @description
 **/
public class ActivityInfo extends AbstractCommand<Response> {

    public String currentActivity(Client client) {
        Request request = new Request();
        Command command = new Command("ActivityInfo", "currentActivity", new Class[0], new Object[0]);
        request.setCommand(command);
        return send(client, request).getData().toString();
    }

    public String currentPackage(Client client) {
        Request request = new Request();
        Command command = new Command("ActivityInfo", "currentPackage", new Class[0], new Object[0]);
        request.setCommand(command);
        return send(client, request).getData().toString();
    }
}
