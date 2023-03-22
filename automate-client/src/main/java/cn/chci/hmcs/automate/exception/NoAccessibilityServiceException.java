package cn.chci.hmcs.automate.exception;

/**
 * @author 有三
 * @date 2022-08-11 16:01
 * @description
 **/
public class NoAccessibilityServiceException extends ClientException{
    public NoAccessibilityServiceException() {
        super();
    }

    public NoAccessibilityServiceException(String message) {
        super(message);
    }

    public NoAccessibilityServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
