package cn.chci.hmcs.automator.core;

import cn.chci.hmcs.automator.fn.By;
import cn.chci.hmcs.automator.fn.Dump;
import cn.chci.hmcs.automator.fn.NodeActions;
import cn.chci.hmcs.automator.fn.Selector;
import cn.chci.hmcs.automator.model.Node;
import cn.chci.hmcs.automator.socket.Client;
import lombok.SneakyThrows;

import java.util.List;

/**
 * @author 有三
 * @date 2022-08-03 21:17
 * @description
 **/
public class AndroidBot {
    private final Client client;
    private final Selector selector;
    private final Dump dump;
    private final String udid;

    private AndroidBot(String udid) {
        this.udid = udid;
        client = new Client();
        selector = new Selector();
        dump = new Dump();
    }

    @SneakyThrows
    public static AndroidBot createAndroidBotAndConnect(String udid) {
        AndroidBot androidBot = new AndroidBot(udid);
        androidBot.client.start(androidBot.udid);
        return androidBot;
    }

    public String dump() {
        return dump.dump(client);
    }

    public Node findOne(By by) {
        if (by == null) {
            return null;
        }
        return selector.findOne(client, by);
    }

    public Node findOne(By by, boolean inScreen) {
        if (by == null) {
            return null;
        }
        return selector.findOne(client, by, inScreen);
    }

    public List<Node> find(By by) {
        if (by == null) {
            return null;
        }
        return selector.find(client, by);
    }

    public List<Node> find(By by, boolean inScreen) {
        if (by == null) {
            return null;
        }
        return selector.find(client, by, inScreen);
    }

    public NodeActionsWrapper nodeActions() {
        return new NodeActionsWrapper();
    }

    public class NodeActionsWrapper {
        private final NodeActions nodeActions;

        public NodeActionsWrapper() {
            this.nodeActions = new NodeActions();
        }

        public boolean click(Node node) {
            if (node == null) {
                return false;
            }
            return nodeActions.click(client, node);
        }

        public boolean longClick(Node node) {
            if (node == null) {
                return false;
            }
            return nodeActions.longClick(client, node);
        }

        public boolean input(Node node, String text) {
            if (node == null) {
                return false;
            }
            return nodeActions.input(client, node, text);
        }
    }


}
