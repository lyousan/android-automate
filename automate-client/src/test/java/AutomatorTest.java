import cn.chci.hmcs.automate.accessibility.fn.By;
import cn.chci.hmcs.automate.accessibility.fn.Wait;
import cn.chci.hmcs.automate.accessibility.fn.WaitOptions;
import cn.chci.hmcs.automate.core.AndroidBot;
import cn.chci.hmcs.automate.model.Node;
import cn.chci.hmcs.automate.model.Point;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        bot = AndroidBot.createAndConnect("5EN0219529000872");
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
        Node node = wait.until(b -> b.findOne(By.textContains("微信")),new Node());
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
