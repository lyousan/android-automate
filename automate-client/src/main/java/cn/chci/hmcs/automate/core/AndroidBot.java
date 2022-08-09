package cn.chci.hmcs.automate.core;

import cn.chci.hmcs.automate.accessibility.fn.*;
import cn.chci.hmcs.automate.model.Node;
import cn.chci.hmcs.automate.model.Point;
import cn.chci.hmcs.automate.model.Rect;
import cn.chci.hmcs.automate.socket.Client;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * @author 有三
 * @date 2022-08-03 21:17
 * @description
 **/
@Slf4j
public class AndroidBot {
    @Getter
    private final Client client;
    @Getter
    private final Global global;
    @Getter
    private final ActivityInfo activityInfo;
    @Getter
    private final Selector selector;
    @Getter
    private final Dump dump;
    @Getter
    private final Device device;
    @Getter
    private final String udid;


    private AndroidBot(String udid) {
        this.udid = udid;
        client = new Client();
        global = new Global();
        activityInfo = new ActivityInfo();
        selector = new Selector();
        dump = new Dump();
        device = new Device();
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
        try {
            return selector.findOne(client, by);
        } catch (InterruptedException e) {
            return null;
        }
    }

    public Node findOne(By by, boolean inScreen) {
        if (by == null) {
            return null;
        }
        try {
            return selector.findOne(client, by, inScreen);
        } catch (InterruptedException e) {
            return null;
        }
    }

    public List<Node> find(By by) {
        if (by == null) {
            return null;
        }
        try {
            return selector.find(client, by);
        } catch (InterruptedException e) {
            return Collections.emptyList();
        }
    }

    public List<Node> find(By by, boolean inScreen) {
        if (by == null) {
            return null;
        }
        try {
            return selector.find(client, by, inScreen);
        } catch (InterruptedException e) {
            return Collections.emptyList();
        }
    }

    public void setSelectWaitOptions(WaitOptions waitOptions) {
        selector.setWaitOptions(waitOptions);
    }

    public WaitOptions getSelectWaitOptions() {
        return selector.getWaitOptions();
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

    public String getClipboardText() {
        return global.getClipboardText(client);
    }

    public boolean setClipboardText(String text) {
        return global.setClipboardText(client, text);
    }

    public boolean click(Point point) {
        return global.click(client, point);
    }

    public boolean longClick(Point point, Integer duration) {
        return global.longClick(client, point, duration);
    }

    public boolean swipe(Point start, Point end, Integer duration) {
        return global.swipe(client, start, end, duration);
    }

    ////////////////////////////////////////////////////

    public String currentActivity() {
        return activityInfo.currentActivity(client);
    }

    public String currentPackage() {
        return activityInfo.currentPackage(client);
    }

    ////////////////////////////////////////////////////

    public Rect getScreenSize() {
        return device.getScreenSize(client);
    }

    ////////////////////////////////////////////////////
}
