package cn.chci.hmcs.automate.dto;

import com.google.gson.Gson;

import cn.chci.hmcs.automate.model.Command;

public class Request {
    // TODO 额外的一些信息

    private String id;

    private Command command;
    private long timestamp;

    public static Request convertToRequest(String content) {
        return new Gson().fromJson(content, Request.class);
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

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
