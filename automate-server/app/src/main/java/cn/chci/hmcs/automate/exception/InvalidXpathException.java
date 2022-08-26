package cn.chci.hmcs.automate.exception;

/**
 * @author 有三
 * @date 2022-08-26 19:51
 * @description
 **/
public class InvalidXpathException extends CustomException {
    public InvalidXpathException() {
        super();
    }

    public InvalidXpathException(String message) {
        super(message);
    }

    public InvalidXpathException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return 504;
    }

    @Override
    public String getDescription() {
        return "无效的xpath表达式";
    }
}
