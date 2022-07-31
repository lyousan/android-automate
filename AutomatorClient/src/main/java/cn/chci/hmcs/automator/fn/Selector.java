package cn.chci.hmcs.automator.fn;

import cn.chci.hmcs.automator.core.ClientContextHolder;
import cn.chci.hmcs.automator.core.ReceiveListener;
import cn.chci.hmcs.automator.core.ReceiveListenerContextHolder;
import cn.chci.hmcs.automator.core.ThreadLocalContextHolder;
import cn.chci.hmcs.automator.dto.Request;
import cn.chci.hmcs.automator.dto.Response;
import cn.chci.hmcs.automator.model.Command;
import cn.chci.hmcs.automator.model.Node;
import cn.chci.hmcs.automator.socket.Client;
import cn.chci.hmcs.automator.utils.NodeParser;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Selector extends Command implements ReceiveListener<Response> {
    /**
     * 等待时间 30s
     */
    private static final long WAIT_TIME = 300L;
    private final CountDownLatch cdl = new CountDownLatch(1);
    private Map<String, Object> result = new HashMap<>();

    /*public String findOne(By by) {
        Client client = ClientContextHolder.get(ThreadLocalContextHolder.getCurrentClientId());
        Command command = new Command("Selector", "findOne", new Class[]{By.class}, new Object[]{by});
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
        if (request != null) {
            return result.get(request.getId()).toString();
        }
        return null;
    }*/

    public Node findOne(By by) {
        Client client = ClientContextHolder.get(ThreadLocalContextHolder.getCurrentClientId());
        Command command = new Command("Selector", "findOne", new Class[]{By.class}, new Object[]{by});
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
        if (request != null) {
            return NodeParser.parse(result.get(request.getId()).toString());
        }
        return null;
    }

    public Node findOne(By by, Boolean inScreen) {
        Client client = ClientContextHolder.get(ThreadLocalContextHolder.getCurrentClientId());
        Command command = new Command("Selector", "findOne", new Class[]{By.class, Boolean.class}, new Object[]{by, inScreen});
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
        if (request != null) {
            return NodeParser.parse(result.get(request.getId()).toString());
        }
        return null;
    }

    public List<Node> find(By by) {
        Client client = ClientContextHolder.get(ThreadLocalContextHolder.getCurrentClientId());
        Command command = new Command("Selector", "find", new Class[]{By.class}, new Object[]{by});
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
        List<Node> nodes = new ArrayList<>();
        if (request != null) {
            if (result.get(request.getId()) instanceof List) {
                for (String s : ((List<String>) result.get(request.getId()))) {
                    nodes.add(NodeParser.parse(s));
                }
            }
        }
        return nodes;
    }

    @Override
    public void onReceive(Response response) {
        result.put(response.getRequestId(), response.getData());
        cdl.countDown();
    }

}
