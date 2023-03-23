package cn.chci.hmcs.automate.accessibility.fn;

import cn.chci.hmcs.automate.dto.Request;
import cn.chci.hmcs.automate.dto.Response;
import cn.chci.hmcs.automate.exception.ServerException;
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
public class Selector extends AbstractCommand<Response>{
    @Setter
    @Getter
    private Wait wait;

    public Selector(Wait wait) {
        this.wait = wait;
    }

    public Node findOne(Client client, By by) {
        if (by == null) {
            return null;
        }
        return findOne(client, by, true);
    }

    public Node findOne(Client client, By by, Boolean inScreen) {
        if (by == null || inScreen == null) {
            return null;
        }
        Node node = null;
        Request request = new Request();
        Command command = new Command("Selector", "findOne", new Class[]{By.class, Boolean.class}, new Object[]{by, inScreen});
        request.setCommand(command);
        try {
            Response response = send(client, request);
            if (response != null && response.getData() != null) {
                node = NodeParser.parse(response.getData().toString(), client);
            }
        } catch (ServerException ignore) {
        }
        // 第一次没有成功获取到时进行隐式重试
        if (node == null) {
            node = wait.implicitUntil(c -> {
                Response response = send(c, request);
                if (response != null && response.getData() != null) {
                    return NodeParser.parse(response.getData().toString(), c);
                }
                return null;
            }, null);
        }
        return node;
    }

    public List<Node> find(Client client, By by) {
        if (by == null) {
            return Collections.emptyList();
        }
        return find(client, by, true);
    }

    public List<Node> find(Client client, By by, Boolean inScreen) {
        if (by == null || inScreen == null) {
            return Collections.emptyList();
        }
        List<Node> nodes = new ArrayList<>();
        Request request = new Request();
        Command command = new Command("Selector", "find", new Class[]{By.class, Boolean.class}, new Object[]{by, inScreen});
        request.setCommand(command);
        try {
            Response response = send(client, request);
            if (response != null && response.getData() instanceof List) {
                for (Object s : ((List<?>) response.getData())) {
                    nodes.add(NodeParser.parse(s.toString(), client));
                }
            }
        } catch (ServerException ignore) {
        }
        // 第一次没有成功获取到时进行隐式重试
        if (nodes.isEmpty()) {
            wait.implicitUntil(c -> {
                Response response = send(c, request);
                if (response != null && response.getData() instanceof List) {
                    for (Object s : ((List<?>) response.getData())) {
                        nodes.add(NodeParser.parse(s.toString(), client));
                    }
                }
                return nodes;
            }, Collections.emptyList());
        }
        return nodes;
    }

}
