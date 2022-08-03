package cn.chci.hmcs.automator.exception;

public class LayoutInspectorNotInitException extends AutomatorException {

    public LayoutInspectorNotInitException() {
        super();
        code = 501;
        description = "布局审查器还未初始化，请检查无障碍服务是否正常，无障碍服务本身存在一些BUG，也许你需要重启设备才能解决";
    }

    public LayoutInspectorNotInitException(String message) {
        super(message);
    }

    public LayoutInspectorNotInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
