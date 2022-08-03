package cn.chci.hmcs.automator.exception;

public class AutomatorException extends RuntimeException {
    public int code = 500;
    public String description = "内置异常";

    public AutomatorException() {
        super();
    }

    public AutomatorException(String message) {
        super(message);
    }

    public AutomatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutomatorException(Throwable cause) {
        super(cause);
    }

    protected AutomatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
