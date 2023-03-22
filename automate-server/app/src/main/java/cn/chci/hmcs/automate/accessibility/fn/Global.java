package cn.chci.hmcs.automate.accessibility.fn;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Looper;

import cn.chci.hmcs.automate.accessibility.clipboard.ClipboardProvider;
import cn.chci.hmcs.automate.model.Command;
import cn.chci.hmcs.automate.model.Point;
import cn.chci.hmcs.automate.utils.AccessibilityServiceGetter;
import cn.chci.hmcs.automate.utils.AccessibilityServiceUtils;
import cn.chci.hmcs.automate.utils.BeanContextHolder;

public class Global extends Command {
    private BeanContextHolder beanContextHolder = BeanContextHolder.getInstance();
    private AccessibilityServiceUtils accessibilityServiceUtils;

    public Global() {
        accessibilityServiceUtils = (AccessibilityServiceUtils) beanContextHolder.getBean("accessibilityServiceUtils");
    }

    /**
     * 跳转到无障碍的设置界面
     */
    public void gotoAccessibilitySettings() {
        accessibilityServiceUtils.gotoAccessibilitySetting();
    }

    public String ping() {
        return "pong";
    }

    /**
     * 回退
     *
     * @return
     */
    public boolean back() {
        return AccessibilityServiceGetter.getInstance().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    /**
     * 回到主页
     *
     * @return
     */
    public boolean home() {
        return AccessibilityServiceGetter.getInstance().performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }


    /**
     * 打开最近的任务
     *
     * @return
     */
    public boolean recents() {
        return AccessibilityServiceGetter.getInstance().performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
    }

    /**
     * 获取剪贴板的内容
     *
     * @return
     */
    public String getClipboardText() {
        return ((ClipboardProvider) beanContextHolder.getBean("clipboardProvider")).getClipboardText();
    }

    /**
     * 设置剪贴板的内容
     */
    public boolean setClipboardText(String text) {
        return ((ClipboardProvider) beanContextHolder.getBean("clipboardProvider")).setClipboardText(text);
    }

    //通过坐标点击
    public static boolean click(Point point) {
        Path path = new Path();
        path.moveTo(point.x, point.y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder.addStroke(new GestureDescription.StrokeDescription(path, 0, 200)).build();
        return AccessibilityServiceGetter.getInstance().dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }
        }, null);
    }

    //通过坐标长按
    public static boolean longClick(Point point, Integer duration) {
        Path path = new Path();
        path.moveTo(point.x, point.y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder.addStroke(new GestureDescription.StrokeDescription(path, 0, duration)).build();
        return AccessibilityServiceGetter.getInstance().dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }
        }, null);
    }

    //自定义滑动
    public static boolean swipe(Point start, Point end, Integer duration) {
        Path path = new Path();
        path.moveTo(start.x, start.y);
        path.lineTo(end.x, end.y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder.addStroke(new GestureDescription.StrokeDescription(path, 0, duration)).build();
        return AccessibilityServiceGetter.getInstance().dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }
        }, null);
    }

}
