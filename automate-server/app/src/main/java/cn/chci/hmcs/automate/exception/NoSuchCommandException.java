package cn.chci.hmcs.automate.exception;

public class NoSuchCommandException extends CustomException {
    public NoSuchCommandException() {
        super();
    }

    public NoSuchCommandException(String message) {
        super(message);
    }

    @Override
    public int getCode() {
        return 401;
    }

    @Override
    public String getDescription() {
        return "没有找到符合的命令";
    }
}
