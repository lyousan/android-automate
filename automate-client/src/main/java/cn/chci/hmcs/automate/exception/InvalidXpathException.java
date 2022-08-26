package cn.chci.hmcs.automate.exception;

/**
 * @author 有三
 * @date 2022-08-26 19:51
 * @description
 **/
public class InvalidXpathException extends ClientException {
    public InvalidXpathException() {
        super();
        code = 504;
        description = "无效的xpath表达式";
    }

    public InvalidXpathException(String message) {
        super(message);
    }

    public InvalidXpathException(String message, Throwable cause) {
        super(message, cause);
    }
}
