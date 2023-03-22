package cn.chci.hmcs.automate.accessibility.fn;

import cn.chci.hmcs.automate.dto.Request;
import cn.chci.hmcs.automate.dto.Response;
import cn.chci.hmcs.automate.model.Command;
import cn.chci.hmcs.automate.model.Point;
import cn.chci.hmcs.automate.socket.Client;


/**
 * @author 有三
 * @date 2022-08-05 12:23
 * @description
 **/
public class Global extends AbstractCommand<Response> {

    public boolean ping(Client client) {
        Request request = new Request();
        request.setCommand(new Command("Global", "ping", new Class[0], new Object[0]));
        Response response = send(client, request);
        return "pong".equals(response.getData().toString());
    }

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

    public String getClipboardText(Client client) {
        Request request = new Request();
        Command command = new Command("Global", "getClipboardText", new Class[0], new Object[0]);
        request.setCommand(command);
        Response response = send(client, request);
        return response.getData().toString();
    }

    public boolean setClipboardText(Client client, String text) {
        if (text == null) {
            return false;
        }
        Request request = new Request();
        Command command = new Command("Global", "setClipboardText", new Class[]{String.class}, new Object[]{text});
        request.setCommand(command);
        Response response = send(client, request);
        return Boolean.parseBoolean(response.getData().toString());
    }

    public boolean click(Client client, Point point) {
        if (point == null) {
            return false;
        }
        Request request = new Request();
        Command command = new Command("Global", "click", new Class[]{Point.class}, new Object[]{point});
        request.setCommand(command);
        Response response = send(client, request);
        return Boolean.parseBoolean(response.getData().toString());
    }

    public boolean longClick(Client client, Point point, Integer duration) {
        if (point == null || duration == null) {
            return false;
        }
        Request request = new Request();
        Command command = new Command("Global", "longClick", new Class[]{Point.class, Integer.class}, new Object[]{point, duration});
        request.setCommand(command);
        Response response = send(client, request);
        return Boolean.parseBoolean(response.getData().toString());
    }

    public boolean swipe(Client client, Point start, Point end, Integer duration) {
        if (start == null || end == null || duration == null) {
            return false;
        }
        Request request = new Request();
        Command command = new Command("Global", "swipe", new Class[]{Point.class, Point.class, Integer.class}, new Object[]{start, end, duration});
        request.setCommand(command);
        Response response = send(client, request);
        return Boolean.parseBoolean(response.getData().toString());
    }
}
