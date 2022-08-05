package cn.chci.hmcs.automator.exception;

public class NoSuchCommandException extends AutomatorException {
    public NoSuchCommandException() {
        super();
        code = 401;
        description = "没有找到符合的命令";
    }

    public NoSuchCommandException(String message) {
        super(message);
    }
}
