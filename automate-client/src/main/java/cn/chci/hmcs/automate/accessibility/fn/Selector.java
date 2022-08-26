package cn.chci.hmcs.automate.accessibility.fn;

import cn.chci.hmcs.automate.dto.Request;
import cn.chci.hmcs.automate.dto.Response;
import cn.chci.hmcs.automate.model.Command;
import cn.chci.hmcs.automate.model.Node;
import cn.chci.hmcs.automate.socket.Client;
import cn.chci.hmcs.automate.utils.NodeParser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class Selector extends AbstractCommand<Response> {
    @Setter
    @Getter
    private WaitOptions waitOptions = WaitOptions.DEFAULT_WAIT_OPTIONS();

    public Node findOne(Client client, By by) throws InterruptedException {
        if (by == null) {
            return null;
        }
        return findOne(client, by, true);
    }

    public Node findOne(Client client, By by, Boolean inScreen) throws InterruptedException {
        if (by == null || inScreen == null) {
            return null;
        }
        long stopTime = waitOptions.calcStopTime();
        Node result = null;
        Request request = new Request();
        Command command = new Command("Selector", "findOne", new Class[]{By.class, Boolean.class}, new Object[]{by, inScreen});
        request.setCommand(command);
        do {
            Response response = send(client, request);
            if (response != null && response.getData() != null) {
                result = NodeParser.parse(response.getData().toString(), client);
            }
            waitOptions.waiting();
        } while (result == null && stopTime >= System.currentTimeMillis());
        return result;
    }

    public List<Node> find(Client client, By by) throws InterruptedException {
        if (by == null) {
            return Collections.emptyList();
        }
        return find(client, by, true);
    }

    public List<Node> find(Client client, By by, Boolean inScreen) throws InterruptedException {
        if (by == null || inScreen == null) {
            return Collections.emptyList();
        }
        long stopTime = waitOptions.calcStopTime();
        List<Node> nodes = new ArrayList<>();
        Request request = new Request();
        Command command = new Command("Selector", "find", new Class[]{By.class, Boolean.class}, new Object[]{by, inScreen});
        request.setCommand(command);
        do {
            Response response = send(client, request);
            if (response != null && response.getData() instanceof List) {
                for (Object s : ((List<?>) response.getData())) {
                    nodes.add(NodeParser.parse(s.toString(), client));
                }
            }
            waitOptions.waiting();
        } while (nodes.isEmpty() && stopTime >= System.currentTimeMillis());
        return nodes;
    }

}
