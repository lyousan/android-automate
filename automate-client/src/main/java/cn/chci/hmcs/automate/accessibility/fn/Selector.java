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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

@Slf4j
public class Selector extends AbstractCommand<Response> {
    @Setter
    @Getter
    private Wait wait;

    public Selector(Wait wait) {
        this.wait = wait;
    }

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
        return wait.implicitUntil(c -> {
            Request request = new Request();
            Command command = new Command("Selector", "findOne", new Class[]{By.class, Boolean.class}, new Object[]{by, inScreen});
            request.setCommand(command);
            Response response = send(c, request);
            if (response != null && response.getData() != null) {
                return NodeParser.parse(response.getData().toString(), c);
            }
            return null;
        }, null);
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
        return wait.implicitUntil(c -> {
            List<Node> nodes = new ArrayList<>();
            Request request = new Request();
            Command command = new Command("Selector", "find", new Class[]{By.class, Boolean.class}, new Object[]{by, inScreen});
            request.setCommand(command);
            Response response = send(client, request);
            if (response != null && response.getData() instanceof List) {
                for (Object s : ((List<?>) response.getData())) {
                    nodes.add(NodeParser.parse(s.toString(), client));
                }
            }
            return nodes;
        }, Collections.emptyList());
    }

}
