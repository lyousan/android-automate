package cn.chci.hmcs.automator.fn;


import cn.chci.hmcs.automator.core.ClientContextHolder;
import cn.chci.hmcs.automator.core.ReceiveListener;
import cn.chci.hmcs.automator.core.ReceiveListenerContextHolder;
import cn.chci.hmcs.automator.core.ThreadLocalContextHolder;
import cn.chci.hmcs.automator.dto.Request;
import cn.chci.hmcs.automator.dto.Response;
import cn.chci.hmcs.automator.model.Command;
import cn.chci.hmcs.automator.socket.Client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Dump extends Command implements ReceiveListener<Response> {
    /**
     * 等待时间 30s
     */
    private static final long WAIT_TIME = 30L;
    private final CountDownLatch cdl = new CountDownLatch(1);
    private String result;

    public String dump() {
        result = null;
        Client client = ClientContextHolder.get(ThreadLocalContextHolder.getCurrentClientId());
        Command command = new Command("Dump", "dump", new Class[0], new Object[0]);
        Request request = null;
        try {
            request = new Request();
            request.setCommand(command);
            ReceiveListenerContextHolder.register(request.getId(), this);
            client.emit(request);
            if (!cdl.await(WAIT_TIME, TimeUnit.SECONDS)) {
                System.out.println("超时");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (request != null) {
                ReceiveListenerContextHolder.remove(request.getId());
            }
        }
        return result;
    }

    @Override
    public void onReceive(Response response) {
        result = response.getData().toString();
        cdl.countDown();
    }
}
