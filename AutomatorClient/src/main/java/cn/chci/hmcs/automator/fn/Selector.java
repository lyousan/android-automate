package cn.chci.hmcs.automator.fn;

import cn.chci.hmcs.automator.dto.Request;
import cn.chci.hmcs.automator.dto.Response;
import cn.chci.hmcs.automator.model.Command;
import cn.chci.hmcs.automator.model.Node;
import cn.chci.hmcs.automator.utils.NodeParser;

import java.util.ArrayList;
import java.util.List;

public class Selector extends AbstractCommand<Response> {

    public Node findOne(By by) {
        return findOne(by, true);
    }

    public Node findOne(By by, Boolean inScreen) {
        Request request = new Request();
        Command command = new Command("Selector", "findOne", new Class[]{By.class, Boolean.class}, new Object[]{by, inScreen});
        request.setCommand(command);
        Response response = send(request);
        if (response == null) {
            return null;
        }
        return NodeParser.parse(response.getData().toString());
    }

    public List<Node> find(By by) {
        return find(by, true);
    }

    public List<Node> find(By by, Boolean inScreen) {
        List<Node> nodes = new ArrayList<>();
        Request request = new Request();
        Command command = new Command("Selector", "find", new Class[]{By.class, Boolean.class}, new Object[]{by, inScreen});
        request.setCommand(command);
        Response response = send(request);
        if (response != null && response.getData() instanceof List) {
            for (Object s : ((List<?>) response.getData())) {
                nodes.add(NodeParser.parse(s.toString()));
            }
        }
        return nodes;
    }

}
