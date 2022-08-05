package cn.chci.hmcs.automator.accessibility.fn;


import cn.chci.hmcs.automator.dto.Request;
import cn.chci.hmcs.automator.dto.Response;
import cn.chci.hmcs.automator.model.Command;
import cn.chci.hmcs.automator.socket.Client;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Dump extends AbstractCommand<Response> {

    public String dump(Client client) {
        Request request = new Request();
        Command command = new Command("Dump", "dump", new Class[0], new Object[0]);
        request.setCommand(command);
        Response response = send(client, request);
        return response.getData().toString();
    }

}
