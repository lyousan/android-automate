package cn.chci.hmcs.automator.fn;

import cn.chci.hmcs.automator.dto.Request;
import cn.chci.hmcs.automator.dto.Response;
import cn.chci.hmcs.automator.model.Command;
import cn.chci.hmcs.automator.model.Node;

public class Actions extends AbstractCommand<Response> {

    public void click(Node node) {
        Request request = new Request();
        Command command = new Command("Actions", "click", new Class[]{String.class}, new Object[]{node.getCacheId()});
        request.setCommand(command);
        Response response = send(request);
        if (response.getCode() == 500) {
            System.out.println(response.getData());
            throw new RuntimeException("error");
        }
//        System.out.println(JSON.toJSONString(response, JSONWriter.Feature.PrettyFormat));
    }

    public void input(Node node, String text) {
        Request request = new Request();
        Command command = new Command("Actions", "input", new Class[]{String.class, String.class}, new Object[]{node.getCacheId(), text});
        request.setCommand(command);
        Response response = send(request);
        if (response.getCode() == 500) {
            System.out.println(response.getData());
            throw new RuntimeException("error");
        }
    }
}
