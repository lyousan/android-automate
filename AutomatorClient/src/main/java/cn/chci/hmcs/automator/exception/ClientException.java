package cn.chci.hmcs.automator.exception;

/**
 * @author 有三
 * @date 2022-08-02 20:55
 * @description
 **/
public class ClientException extends RuntimeException{
    public ClientException() {
        super();
    }

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientException(Throwable cause) {
        super(cause);
    }

    protected ClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
