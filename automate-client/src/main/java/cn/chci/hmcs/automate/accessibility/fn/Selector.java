package cn.chci.hmcs.automate.accessibility.fn;

import cn.chci.hmcs.automate.dto.Request;
import cn.chci.hmcs.automate.dto.Response;
import cn.chci.hmcs.automate.model.Command;
import cn.chci.hmcs.automate.model.Node;
import cn.chci.hmcs.automate.socket.Client;
import cn.chci.hmcs.automate.utils.NodeParser;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Selector extends AbstractCommand<Response> {

    public Node findOne(Client client, By by) {
        return findOne(client, by, true);
    }

    public Node findOne(Client client, By by, Boolean inScreen) {
        Request request = new Request();
        Command command = new Command("Selector", "findOne", new Class[]{By.class, Boolean.class}, new Object[]{by, inScreen});
        request.setCommand(command);
        Response response = send(client, request);
        if (response == null) {
            return null;
        }
        return NodeParser.parse(response.getData().toString(),client);
    }

    public List<Node> find(Client client, By by) {
        return find(client, by, true);
    }

    public List<Node> find(Client client, By by, Boolean inScreen) {
        List<Node> nodes = new ArrayList<>();
        Request request = new Request();
        Command command = new Command("Selector", "find", new Class[]{By.class, Boolean.class}, new Object[]{by, inScreen});
        request.setCommand(command);
        Response response = send(client, request);
        if (response != null && response.getData() instanceof List) {
            for (Object s : ((List<?>) response.getData())) {
                nodes.add(NodeParser.parse(s.toString(),client));
            }
        }
        return nodes;
    }

}
