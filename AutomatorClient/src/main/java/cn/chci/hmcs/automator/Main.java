package cn.chci.hmcs.automator;

import cn.chci.hmcs.automator.core.ClientContextHolder;
import cn.chci.hmcs.automator.core.ThreadLocalContextHolder;
import cn.chci.hmcs.automator.fn.Actions;
import cn.chci.hmcs.automator.fn.By;
import cn.chci.hmcs.automator.fn.Dump;
import cn.chci.hmcs.automator.fn.Selector;
import cn.chci.hmcs.automator.model.Node;
import cn.chci.hmcs.automator.socket.Client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.Scanner;
import java.util.UUID;

/**
 * @Author 有三
 * @Date 2022-07-23 13:12
 * @Description
 **/
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        init();
//        long begin = System.currentTimeMillis();
//        String dump = new Dump().dump();
//        System.out.println("dump: " + dump);
//        long end = System.currentTimeMillis();
//        System.out.println("take " + (end - begin) + "ms");

//        String findOne = new Selector().findOne(By.xpath("//*[not(@text=\"\")]"));
//        Node findOne = new Selector().findOne(By.id("com.tencent.mm:id/g5g"));
//        System.out.println("findOne: " + findOne);

//        List<Node> find = new Selector().find(By.className("android.widget.TextView"));
//        System.out.println("find: " + find);
//        find.forEach(node -> System.out.println(node.getText()));
        Scanner scanner = new Scanner(System.in);
        String cmd = null;
        while ((cmd = scanner.next()) != null && !"exit".equalsIgnoreCase(cmd)) {
            if ("dump".equalsIgnoreCase(cmd)) {
                dump();
            } else if (cmd.contains("findOne")) {
                Node node = findOne(cmd.split("==")[1], Boolean.parseBoolean(cmd.split("==")[2]));
                new Actions().click(node);
                new Actions().input(node, "hello");
                System.out.println("123");
            }
        }
        System.exit(0);
    }

    private static void dump() {
        long begin = System.currentTimeMillis();
        String dump = new Dump().dump();
        System.out.println("dump: " + dump);
        long end = System.currentTimeMillis();
        System.out.println("take " + (end - begin) + "ms");
    }

    private static Node findOne(String xpath, boolean inScreen) {
        long begin = System.currentTimeMillis();
        Node findOne = new Selector().findOne(By.xpath(xpath), inScreen);
        if (findOne != null) {
            System.out.println("findOne.getText: " + findOne.getText());
        } else {
            System.out.println("There are no such node in current window");
        }
        long end = System.currentTimeMillis();
        System.out.println("take " + (end - begin) + "ms");
        return findOne;
    }

    private static void init() throws IOException, InterruptedException {
        Properties properties = new Properties();
        properties.load(Files.newInputStream(new File("resources/config.properties").toPath()));
        Client client = new Client();
        client.start(properties.getProperty("udid"));
        String clientId = UUID.randomUUID().toString();
        ThreadLocalContextHolder.setCurrentClientId(clientId);
        ClientContextHolder.put(clientId, client);
    }
}
