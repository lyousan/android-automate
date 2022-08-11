package cn.chci.hmcs.automate.dto;

import com.alibaba.fastjson2.JSON;

import cn.chci.hmcs.automate.model.Command;

import java.util.UUID;

public class Request {

    private final String id;
    private Command command;

    private long timestamp;

    public Request() {
        id = UUID.randomUUID().toString();
    }

    public static Request convertToRequest(String content) {
        return JSON.parseObject(content, Request.class);
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
