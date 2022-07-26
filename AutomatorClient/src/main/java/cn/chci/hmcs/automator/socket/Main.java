package cn.chci.hmcs.automator.socket;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

/**
 * @Author 有三
 * @Date 2022-07-23 13:12
 * @Description
 **/
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Properties properties = new Properties();
        properties.load(Files.newInputStream(new File("resources/config.properties").toPath()));
        Client.start(properties.getProperty("udid"));
    }
}
