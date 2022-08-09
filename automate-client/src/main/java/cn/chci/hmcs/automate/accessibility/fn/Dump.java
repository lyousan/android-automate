package cn.chci.hmcs.automate.accessibility.fn;


import cn.chci.hmcs.automate.dto.Request;
import cn.chci.hmcs.automate.dto.Response;
import cn.chci.hmcs.automate.model.Command;
import cn.chci.hmcs.automate.socket.Client;
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
