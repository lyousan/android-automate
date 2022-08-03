package cn.chci.hmcs.automator.fn;

import cn.chci.hmcs.automator.dto.Request;
import cn.chci.hmcs.automator.dto.Response;
import cn.chci.hmcs.automator.model.Command;
import cn.chci.hmcs.automator.model.Node;
import cn.chci.hmcs.automator.socket.Client;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NodeActions extends AbstractCommand<Response> {

    public boolean click(Client client, Node node) {
        Request request = new Request();
        Command command = new Command("NodeActions", "click", new Class[]{String.class}, new Object[]{node.getCacheId()});
        request.setCommand(command);
        Response response = send(client, request);
        return Boolean.parseBoolean(response.getData().toString());
    }

    public boolean longClick(Client client, Node node) {
        Request request = new Request();
        Command command = new Command("NodeActions", "longClick", new Class[]{String.class}, new Object[]{node.getCacheId()});
        request.setCommand(command);
        Response response = send(client, request);
        return Boolean.parseBoolean(response.getData().toString());
    }

    public boolean input(Client client, Node node, String text) {
        Request request = new Request();
        Command command = new Command("NodeActions", "input", new Class[]{String.class, String.class}, new Object[]{node.getCacheId(), text});
        request.setCommand(command);
        Response response = send(client, request);
        return Boolean.parseBoolean(response.getData().toString());
    }
}
