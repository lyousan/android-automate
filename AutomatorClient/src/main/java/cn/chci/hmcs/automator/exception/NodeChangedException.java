package cn.chci.hmcs.automator.exception;

public class NodeChangedException extends AutomatorException {
    public NodeChangedException() {
        super();
        code = 502;
        description = "节点可能已经发生了改变，当界面发生改变后请尝试重新获取元素";
    }

    public NodeChangedException(String message) {
        super(message);
    }

    public NodeChangedException(String message, Throwable cause) {
        super(message, cause);
    }
}
