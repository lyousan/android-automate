package cn.chci.hmcs.automate.exception;

public class CustomException extends RuntimeException {
    protected static final int DEFAULT_CODE = 500;
    protected static final String DEFAULT_DESCRIPTION = "内置异常";

    public int getCode() {
        return DEFAULT_CODE;
    }

    public String getDescription() {
        return DEFAULT_DESCRIPTION;
    }

    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    protected CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
