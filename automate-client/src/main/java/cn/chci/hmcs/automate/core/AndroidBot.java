package cn.chci.hmcs.automate.core;

import cn.chci.hmcs.automate.accessibility.fn.*;
import cn.chci.hmcs.automate.model.Node;
import cn.chci.hmcs.automate.model.Point;
import cn.chci.hmcs.automate.model.Rect;
import cn.chci.hmcs.automate.socket.Client;
import cn.chci.hmcs.common.toolkit.utils.AdbUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.Socket;
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


    private AndroidBot(String udid, Integer pcPort, Integer androidPort) {
        this.udid = udid;
        client = new Client(udid, pcPort, androidPort);
        global = new Global();
        activityInfo = new ActivityInfo();
        selector = new Selector();
        dump = new Dump();
        device = new Device();
    }

    @SneakyThrows
    public static AndroidBot createAndConnect(String udid) {
        return createAndConnect(udid, false);
    }

    @SneakyThrows
    public static AndroidBot createAndConnect(String udid, boolean closeExistServer) {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(0);
        Socket temp = new Socket();
        temp.bind(inetSocketAddress);
        int pcPort = temp.getLocalPort();
        temp.close();
        AndroidBot androidBot = new AndroidBot(udid, pcPort, Client.DEFAULT_ANDROID_PORT);
        androidBot.client.connect(closeExistServer);
        return androidBot;
    }

    public void close() {
        client.close();
    }

    public String dump() {
        return dump.dump(client);
    }

    @SneakyThrows
    public Node findOne(By by) {
        if (by == null) {
            return null;
        }
        return selector.findOne(client, by);
    }

    @SneakyThrows
    public Node findOne(By by, boolean inScreen) {
        if (by == null) {
            return null;
        }
        return selector.findOne(client, by, inScreen);
    }

    @SneakyThrows
    public List<Node> find(By by) {
        if (by == null) {
            return null;
        }
        return selector.find(client, by);
    }

    @SneakyThrows
    public List<Node> find(By by, boolean inScreen) {
        if (by == null) {
            return null;
        }
        return selector.find(client, by, inScreen);
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
        String currentInputMethod = AdbUtils.getInputMethod(udid);
        try {
            AdbUtils.setInputMethod("cn.chci.hmcs.automate/.AutomateIMEService", udid);
            return global.getClipboardText(client);
        } finally {
            AdbUtils.setInputMethod(currentInputMethod, udid);
        }

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
