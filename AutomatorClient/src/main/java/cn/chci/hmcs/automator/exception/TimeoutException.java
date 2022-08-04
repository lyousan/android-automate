package cn.chci.hmcs.automator.exception;

/**
 * @author 有三
 * @date 2022-08-04 10:45
 * @description
 **/
public class TimeoutException extends ServerException{
    public TimeoutException() {
        super();
    }

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
