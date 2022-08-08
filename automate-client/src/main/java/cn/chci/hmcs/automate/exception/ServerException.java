package cn.chci.hmcs.automate.exception;

/**
 * @author 有三
 * @date 2022-08-02 20:55
 * @description
 **/
public class ServerException extends RuntimeException{
    public ServerException() {
        super();
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
