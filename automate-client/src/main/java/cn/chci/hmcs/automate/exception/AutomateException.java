package cn.chci.hmcs.automate.exception;

public class AutomateException extends RuntimeException {
    public int code = 500;
    public String description = "内置异常";

    public AutomateException() {
        super();
    }

    public AutomateException(String message) {
        super(message);
    }

    public AutomateException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutomateException(Throwable cause) {
        super(cause);
    }

    protected AutomateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
