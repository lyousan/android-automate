package cn.chci.hmcs.automator.exception;

public class NodeInoperableException extends CustomException {
    public NodeInoperableException() {
        super();
        code = 503;
        description = "该元素不支持当前操作";
    }

    public NodeInoperableException(String message) {
        super(message);
    }

    public NodeInoperableException(String message, Throwable cause) {
        super(message, cause);
    }
}
