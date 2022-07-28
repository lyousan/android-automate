package cn.chci.hmcs.automator.exception;

public class NoSuchCommandException extends RuntimeException{
    public NoSuchCommandException() {
        super();
    }

    public NoSuchCommandException(String message) {
        super(message);
    }
}
