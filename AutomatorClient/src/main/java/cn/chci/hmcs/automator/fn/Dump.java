package cn.chci.hmcs.automator.fn;


import cn.chci.hmcs.automator.dto.Request;
import cn.chci.hmcs.automator.dto.Response;
import cn.chci.hmcs.automator.model.Command;

public class Dump extends AbstractCommand<Response> {

    public String dump() {
        Request request = new Request();
        Command command = new Command("Dump", "dump", new Class[0], new Object[0]);
        request.setCommand(command);
        Response response = send(request);
        return response.getData().toString();
    }


}
