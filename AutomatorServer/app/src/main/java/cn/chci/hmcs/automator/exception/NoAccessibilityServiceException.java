package cn.chci.hmcs.automator.exception;

public class NoAccessibilityServiceException extends CustomException {
    @Override
    public int getCode() {
        return 504;
    }

    @Override
    public String getDescription() {
        return "未获取到无障碍服务权限";
    }

    public NoAccessibilityServiceException() {
        super();
    }

    public NoAccessibilityServiceException(String message) {
        super(message);
    }

    public NoAccessibilityServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAccessibilityServiceException(Throwable cause) {
        super(cause);
    }

    protected NoAccessibilityServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
