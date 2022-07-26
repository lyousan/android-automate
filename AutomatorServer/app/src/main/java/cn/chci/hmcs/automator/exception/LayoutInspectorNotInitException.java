package cn.chci.hmcs.automator.exception;

public class LayoutInspectorNotInitException extends RuntimeException {
    public LayoutInspectorNotInitException() {
        super();
    }

    public LayoutInspectorNotInitException(String message) {
        super(message);
    }

    public LayoutInspectorNotInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
