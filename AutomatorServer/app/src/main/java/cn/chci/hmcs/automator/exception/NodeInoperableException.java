package cn.chci.hmcs.automator.exception;

public class NodeInoperableException extends CustomException {
    public NodeInoperableException() {
        super();
    }

    public NodeInoperableException(String message) {
        super(message);
    }

    public NodeInoperableException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return 503;
    }

    @Override
    public String getDescription() {
        return "该元素不支持当前操作";
    }
}
