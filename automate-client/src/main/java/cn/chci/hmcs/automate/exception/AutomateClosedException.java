package cn.chci.hmcs.automate.exception;

/**
 * @author 有三
 * @date 2022-08-30 10:54
 * @description
 **/
public class AutomateClosedException extends ClientException {
    public AutomateClosedException() {
        super();
        code = 505;
        description = "Automate连接已断开";
    }

    public AutomateClosedException(String message) {
        super(message);
    }

    public AutomateClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
