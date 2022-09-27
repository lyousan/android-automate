import cn.chci.hmcs.automate.accessibility.fn.By;
import cn.chci.hmcs.automate.core.AndroidBot;
import cn.chci.hmcs.automate.model.Node;
import cn.chci.hmcs.automate.utils.AdbUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * @author 有三
 * @date 2022-08-04 14:08
 * @description
 **/
@SpringBootTest(classes = WeChatTest.class)
@Slf4j
public class WeChatTest {
    private AndroidBot bot;
    private String udid = "MDX0220427011762";

    @BeforeEach
    void init() {
        bot = AndroidBot.createAndConnect(udid);
        log.info("init");
    }

    @Test
    void startApp() throws IOException, InterruptedException {
        log.info("startApp");
        AdbUtils.exec("adb -s " + udid + " shell am start com.tencent.mm/.ui.LauncherUI");
    }

    @Test
    void gotoSearch() {
        log.info("gotoSearch");
//        bot.findOne(By.id("com.tencent.mm:id/j5t")).click(); 没反应
        Node node = bot.findOne(By.id("com.tencent.mm:id/j5t"));
        AdbUtils.tap(node.getRect().x, node.getRect().y);
    }

    @Test
    void search() throws IOException, InterruptedException {
        log.info("search");
        bot.findOne(By.id("com.tencent.mm:id/cd7")).input("Java");
        AdbUtils.exec("adb -s " + udid + " shell input keyevent 66");
    }

    @Test
    void switchResultToPublic() {
        log.info("switchResultToPublic");
        // 微信这里是webview，不一定立马就能加载处理，如果第一次没有获取到的话可以多获取几次
        bot.findOne(By.textContains("公众号,按钮")).click();
    }

    @Test
    void parsePublicItems() {
        log.info("parsePublicItems");
        List<Node> nodes = bot.find(By.xpath("//*[@resource-id=\"search_result\"]//*[@class=\"android.view.View\"][@content-desc and not(@content-desc=\"\")]"),false);
        nodes.forEach(node -> {
            log.info("描述: {}", node.getContentDesc());
            log.info("坐标: {}", node.getRect());
            log.info("--------------------------------------");
        });
    }

    @Test
    void gotoPublic() {
        log.info("gotoPublic");
        List<Node> nodes = bot.find(By.xpath("//*[@resource-id=\"search_result\"]//*[@class=\"android.view.View\"][@content-desc and not(@content-desc=\"\")]"));
        nodes.get(0).click();
    }

    @Test
    void parseArticleItems() {
        log.info("parseArticleItems");
        List<Node> nodes = bot.find(By.id("com.tencent.mm:id/cs"));
        nodes.forEach(node -> {
            log.info("标题: {}", node.getText());
            log.info("坐标: {}", node.getRect());
            log.info("--------------------------------------");
        });
    }

    @Test
    void gotoArticle() {
        log.info("gotoArticle");
        List<Node> nodes = bot.find(By.id("com.tencent.mm:id/cs"));
        nodes.get(0).click();
    }

    @Test
    void parseArticle() {
        log.info("parseArticle");
        String dump = bot.dump();
        log.info("dump: {}", dump);
        String title = bot.findOne(By.id("activity-name"),false).getText();
        String publisher = bot.findOne(By.id("js_name"),false).getContentDesc();
        String publishTime = bot.findOne(By.id("publish_time"),false).getText();
        String publishArea = bot.findOne(By.id("js_ip_wording_wrp"),false).getText();
        List<Node> contentNodes = bot.find(By.xpath("//*[@resource-id=\"js_content\"]//*[@class=\"android.view.View\"][not(@text=\"\")]"), false);
        StringBuilder builder = new StringBuilder();
        contentNodes.forEach(node -> builder.append(node.getText()));
        String content = builder.toString();
        log.info("标题：{}", title);
        log.info("发布者：{}", publisher);
        log.info("发布时间：{}", publishTime);
        log.info("发布地点：{}", publishArea);
        log.info("正文：{}", content);
    }
}
