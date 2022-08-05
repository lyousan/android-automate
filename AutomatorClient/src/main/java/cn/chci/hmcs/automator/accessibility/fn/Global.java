package cn.chci.hmcs.automator.accessibility.fn;

import cn.chci.hmcs.automator.dto.Request;
import cn.chci.hmcs.automator.dto.Response;
import cn.chci.hmcs.automator.model.Command;
import cn.chci.hmcs.automator.socket.Client;

/**
 * @author 有三
 * @date 2022-08-05 12:23
 * @description
 **/
public class Global extends AbstractCommand<Response> {

    public void gotoAccessibilitySettings(Client client) {
        Request request = new Request();
        Command command = new Command("Global", "gotoAccessibilitySettings", new Class[0], new Object[0]);
        request.setCommand(command);
        send(client, request);
    }

    public boolean back(Client client) {
        Request request = new Request();
        Command command = new Command("Global", "back", new Class[0], new Object[0]);
        request.setCommand(command);
        Response response = send(client, request);
        return Boolean.parseBoolean(response.getData().toString());
    }

    public boolean home(Client client) {
        Request request = new Request();
        Command command = new Command("Global", "home", new Class[0], new Object[0]);
        request.setCommand(command);
        Response response = send(client, request);
        return Boolean.parseBoolean(response.getData().toString());
    }

    public boolean recents(Client client) {
        Request request = new Request();
        Command command = new Command("Global", "recents", new Class[0], new Object[0]);
        request.setCommand(command);
        Response response = send(client, request);
        return Boolean.parseBoolean(response.getData().toString());
    }
}
