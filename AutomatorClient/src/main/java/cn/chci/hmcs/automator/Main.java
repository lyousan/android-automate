package cn.chci.hmcs.automator;

import cn.chci.hmcs.automator.core.ClientContextHolder;
import cn.chci.hmcs.automator.core.ThreadLocalContextHolder;
import cn.chci.hmcs.automator.fn.By;
import cn.chci.hmcs.automator.fn.Dump;
import cn.chci.hmcs.automator.fn.Selector;
import cn.chci.hmcs.automator.model.Node;
import cn.chci.hmcs.automator.socket.Client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
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
        String dump = new Dump().dump();
        System.out.println("dump: " + dump);

//        String findOne = new Selector().findOne(By.xpath("//*[not(@text=\"\")]"));
        Node findOne = new Selector().findOne(By.id("com.tencent.mm:id/g5g"));
        System.out.println("findOne: " + findOne);

        List<Node> find = new Selector().find(By.className("android.widget.TextView"));
        System.out.println("find: " + find);
        find.forEach(node -> System.out.println(node.getText()));
        Scanner scanner = new Scanner(System.in);
        scanner.next();
        System.exit(0);
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
