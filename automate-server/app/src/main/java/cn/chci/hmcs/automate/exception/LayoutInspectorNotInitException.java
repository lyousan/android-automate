package cn.chci.hmcs.automate.exception;

public class LayoutInspectorNotInitException extends CustomException {

    public LayoutInspectorNotInitException() {
        super();
    }

    @Override
    public int getCode() {
        return 501;
    }

    @Override
    public String getDescription() {
        return "布局审查器还未初始化，请检查无障碍服务是否正常，无障碍服务本身存在一些BUG，也许你需要重启设备才能解决";
    }

    public LayoutInspectorNotInitException(String message) {
        super(message);
    }

    public LayoutInspectorNotInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
