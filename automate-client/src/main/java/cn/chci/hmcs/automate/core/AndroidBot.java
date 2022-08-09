package cn.chci.hmcs.automate.core;

import cn.chci.hmcs.automate.accessibility.fn.*;
import cn.chci.hmcs.automate.model.Node;
import cn.chci.hmcs.automate.socket.Client;
import lombok.SneakyThrows;

import java.util.List;

/**
 * @author 有三
 * @date 2022-08-03 21:17
 * @description
 **/
public class AndroidBot {
    private final Client client;

    private final Global global;
    private final ActivityInfo activityInfo;
    private final Selector selector;
    private final Dump dump;
    private final String udid;

    private AndroidBot(String udid) {
        this.udid = udid;
        client = new Client();
        global = new Global();
        activityInfo = new ActivityInfo();
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

    //////////////////////////////////////////////////

    public void gotoAccessibilitySettings() {
        global.gotoAccessibilitySettings(client);
    }

    public boolean back() {
        return global.back(client);
    }

    public boolean home() {
        return global.home(client);
    }

    public boolean recents() {
        return global.recents(client);
    }

    public String currentActivity() {
        return activityInfo.currentActivity(client);
    }

    public String currentPackage() {
        return activityInfo.currentPackage(client);
    }

    public String getClipboardText() {
        return global.getClipboardText(client);
    }

    public boolean setClipboardText(String text) {
        return global.setClipboardText(client, text);
    }

}
