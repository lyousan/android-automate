import cn.chci.hmcs.automator.core.AndroidBot;
import cn.chci.hmcs.automator.fn.By;
import cn.chci.hmcs.automator.model.Node;
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
        AdbUtils.exec("adb -s 5EN0219529000872 shell input keyevent 3");
        bot = AndroidBot.createAndroidBotAndConnect("MDX0220427011762");
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
        List<Node> nodes = bot.find(By.xpath("//*[not(@content-desc=\"\")]"));
        nodes.forEach(node -> log.info(node.getContentDesc()));
    }

    @Test
    void testClick() {
        // 桌面
        Node node = bot.findOne(By.contentDesc("微信"), false);
        log.info("node.rect: {}", node.getRect());
        bot.nodeActions().click(node);
    }

    @Test
    void testLongClick() {
        // 桌面
        Node node = bot.findOne(By.contentDesc("微信"), false);
        log.info("node.rect: {}", node.getRect());
        bot.nodeActions().longClick(node);
    }

    @Test
    void testInput() {
        testClick();
        // 微信登录
        Node node = bot.findOne(By.id("com.tencent.mm:id/cd7"));
        log.info("node.text: {}", node.getText());
        bot.nodeActions().input(node, "Hello World");
        node = bot.findOne(By.id("com.tencent.mm:id/cd7"));
        log.info("node.text: {}", node.getText());
    }
}
