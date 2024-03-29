package cn.chci.hmcs.automate.dto;

import com.google.gson.Gson;

public class Response {
    private int code;
    private String msg;
    private Object data;
    private String requestId;
    private long requestTimestamp;
    private long timestamp = System.currentTimeMillis();

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Response success(String msg, Object data) {
        return new Response(200, msg, data);
    }

    public static Response success(String msg) {
        return new Response(200, msg, null);
    }

    public static Response clientFail(String msg, Object data) {
        return new Response(400, msg, data);
    }

    public static Response clientFail(String msg) {
        return new Response(400, msg, null);
    }

    public static Response serverFail(String msg, Object data) {
        return new Response(500, msg, data);
    }

    public static Response serverFail(String msg) {
        return new Response(500, msg, null);
    }

    public static String convertToJson(Response response) {
        return new Gson().toJson(response);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
