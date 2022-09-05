package cn.chci.hmcs.automate.exception;

public class NoSuchCommandException extends ClientException {
    public NoSuchCommandException() {
        super();
        code = 401;
        description = "没有找到符合的命令";
    }

    public NoSuchCommandException(String message) {
        super(message);
    }
}
