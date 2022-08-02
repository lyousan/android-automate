package cn.chci.hmcs.automator.exception;

public class NodeChangedException extends CustomException {
    public NodeChangedException() {
        super();
    }

    public NodeChangedException(String message) {
        super(message);
    }

    public NodeChangedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return 502;
    }

    @Override
    public String getDescription() {
        return "节点可能已经发生了改变，当界面发生改变后请尝试重新获取元素";
    }
}
