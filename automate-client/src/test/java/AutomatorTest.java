import cn.chci.hmcs.automate.accessibility.fn.By;
import cn.chci.hmcs.automate.core.AndroidBot;
import cn.chci.hmcs.automate.model.Node;
import cn.chci.hmcs.common.toolkit.utils.AdbUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

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
        bot = AndroidBot.createAndroidBotAndConnect("RKAM5L55T8FEHMOV");
    }

    @Test
    void testDump() {
        String dump = bot.dump();
        log.info("dump: {}", dump);
    }

    @Test
    void testFindOne() {
        // 桌面
        Node node = bot.findOne(By.id("com.huawei.android.totemweather:id/widget_dual_single_city_layout"), false);
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
        log.info("testSetClipboardText: {}", bot.setClipboardText("hello world"));
    }
}
