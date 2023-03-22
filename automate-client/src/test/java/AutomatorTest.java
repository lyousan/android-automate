import cn.chci.hmcs.automate.accessibility.fn.By;
import cn.chci.hmcs.automate.accessibility.fn.Wait;
import cn.chci.hmcs.automate.accessibility.fn.WaitOptions;
import cn.chci.hmcs.automate.core.AndroidBot;
import cn.chci.hmcs.automate.model.Node;
import cn.chci.hmcs.automate.model.Point;
import cn.chci.hmcs.automate.utils.AdbUtils;
import cn.chci.hmcs.automate.utils.NodeParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.chci.hmcs.automate.socket.Client.PACKAGE_NAME;

/**
 * @author 有三
 * @date 2022-08-03 21:30
 * @description
 **/
@SpringBootTest(classes = AutomatorTest.class)
@Slf4j
public class AutomatorTest {
    private AndroidBot bot;

    @BeforeEach
    void init() throws IOException, InterruptedException {
        // 退回桌面
//        AdbUtils.exec("adb -s RKAM5L55T8FEHMOV shell input keyevent 3");
        bot = AndroidBot.createAndConnect("OZRWWOR4YPHIHQ4T", false);
    }

    @Test
    void ping() {
        System.out.println(bot.getGlobal().ping(bot.getClient()));
    }

    @Test
    void testClosedException() throws IOException, InterruptedException {
//        bot.dump();
//        boolean closed = bot.getClient().isClosed();
//        Node node = bot.findOne(By.id("123"));
//        AdbUtils.exec("adb -s OZRWWOR4YPHIHQ4T shell am force-stop " + PACKAGE_NAME);
//        closed = bot.getClient().isClosed();
        bot.getSelector().setTimeout(2L);
        bot.setSelectorWaitOption(new WaitOptions(5L, 1L, TimeUnit.SECONDS));
//        Node node = bot.findOne(By.id("123"));
//        List<Node> nodes = bot.find(By.id("123"));
        List<Node> nodes1 = bot.find(By.xpath("123"));
//        bot.dump();
        log.info("123123123");
//        log.info("client==>{}", bot.getClient().isClosed());
    }

    @Test
    void test() throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                AndroidBot bot = AndroidBot.createAndConnect("SKDYJJ7DR47DS879");
                log.info("i ==> {}", finalI);
                String dump = bot.dump();
                log.info("dump ==> {}", dump);
                String currentActivity = bot.currentActivity();
                log.info("currentActivity ==> {}", currentActivity);
                if (finalI % 2 == 0) {
                    bot.close();
                }
            }, i + "-thread");
            thread.start();
            thread.join();
        }
    }

    /**
     * 复现多线程下同时转换Doc时的异常（采用单SAXReader），越复杂的界面耗时越长，越容易复现
     * 改用ThreadLocal后解决了这个情况
     *
     * @throws InterruptedException
     */
    @Test
    void testNodeParser() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            AndroidBot bot = AndroidBot.createAndConnect("5ea890e60407");
            for (int i = 0; i < 1000; i++) {
                NodeParser.parse(bot.dump());
            }
        }, "5ea890e60407");
        Thread thread2 = new Thread(() -> {
            AndroidBot bot = AndroidBot.createAndConnect("MDX0220427011762");
            for (int i = 0; i < 1000; i++) {
                NodeParser.parse(bot.dump());
            }
        }, "MDX0220427011762");
        long begin = System.currentTimeMillis();
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("finish: " + (System.currentTimeMillis() - begin));
    }

    @Test
    void testDump() {
        String dump = bot.dump();
        log.info("dump: {}", dump);
        bot.close();
    }

    @Test
    void testFindOne() {
//        bot.setSelectorWaitOption(new WaitOptions(3, 1, TimeUnit.SECONDS));
        // 桌面
        Wait wait = new Wait(bot, new WaitOptions(10, 1, TimeUnit.SECONDS));
        Node node = wait.until(b -> b.findOne(By.textContains("微信")), new Node());
//        Node node = bot.findOne(By.textEquals("微信"), true);
        log.info("node.text: {}", node.getText());
        log.info("node.rect: {}", node.getRect());
        log.info("node.children: {}", node.getChildren());
    }

    @Test
    void testFind() {
        // 桌面
        List<Node> nodes = bot.find(By.id("com.tencent.mm:id/bdq"));
        nodes.forEach(node -> log.info(node.getContentDesc()));
    }

    @Test
    void testClick() {
        // 桌面
        Node node = bot.findOne(By.textEquals("微信"), false);
        log.info("node.rect: {}", node.getRect());
        node.click();
    }

    @Test
    void testLongClick() {
        // 桌面
        Node node = bot.findOne(By.textEquals("微信"), false);
        log.info("node.rect: {}", node.getRect());
        node.longClick();
    }

    @Test
    void testInput() {
        testClick();
        // 微信登录
        Node node = bot.findOne(By.id("com.tencent.mm:id/cd7"));
        log.info("node.text: {}", node.getText());
        node.input("Hello World");
        node = bot.findOne(By.id("com.tencent.mm:id/cd7"));
        log.info("node.text: {}", node.getText());
    }

    @Test
    void testGotoAccessibilitySettings() {
        bot.gotoAccessibilitySettings();
    }

    @Test
    void testBack() {
        bot.back();
    }

    @Test
    void testHome() {
        bot.home();
    }

    @Test
    void testRecents() {
        bot.recents();
    }

    @Test
    void testCurrentActivity() {
        log.info("currentActivity: {}", bot.currentActivity());
    }

    @Test
    void testCurrentPackage() {
        log.info("currentPackage: {}", bot.currentPackage());
    }

    @Test
    void testGetClipboardText() {
        log.info("testGetClipboardText: {}", bot.getClipboardText());
    }

    @Test
    void testSetClipboardText() {
        log.info("testSetClipboardText: {}", bot.setClipboardText(""));
    }

    @Test
    void testGlobalClick() {
        log.info("testGlobalClick: {}", bot.click(new Point(200, 200)));
    }

    @Test
    void testGlobalLongClick() {
        log.info("testGlobalLongClick: {}", bot.longClick(new Point(124, 360), 5000));
    }

    @Test
    void testGlobalSwipe() {
        log.info("testGlobalSwipe: {}", bot.swipe(new Point(200, 200), new Point(540, 1500), 5000));
    }

    @Test
    void testScreenSize() {
        log.info("testScreenSize: {}", bot.getScreenSize());
    }

    @Test
    void testScrollUp() {
        Node node = bot.findOne(By.id("com.android.settings:id/main_content_scrollable_container"));
        log.info("testScrollUp: {}", node.scrollUp());
    }

    @Test
    void testScrollDown() {
        Node node = bot.findOne(By.id("com.android.settings:id/main_content_scrollable_container"));
        log.info("testScrollDown: {}", node.scrollDown());
    }
}
