package cn.chci.hmcs.automator;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import java.util.List;

import cn.chci.hmcs.automator.layout.LayoutInspector;
import cn.chci.hmcs.automator.utils.BeanContextHolder;

public class MyAccessibilityService extends AccessibilityService {
    private static final String LOG_TAG = "hmcs-automator";
    @SuppressLint("StaticFieldLeak")
    public static AccessibilityService instance;
    private final BeanContextHolder beanContextHolder = BeanContextHolder.getInstance();
    public LayoutInspector layoutInspector;
    private static volatile boolean isCapturing;

    public MyAccessibilityService() {
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // TODO 监听一些事件，暂时没啥用，因为我们才是主动调用方
        Log.i(LOG_TAG, "onAccessibilityEvent: 接收到事件");
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                break;
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
        }
        // 测试代码，这个地方不能这么搞，一直获取会崩的
        if (!isCapturing) {
            synchronized (this) {
                if (!isCapturing) {
                    isCapturing = true;
//                    NodeInfo nodeInfo = layoutInspector.captureCurrentWindow();
//                    String xmlString = NodeInfoParser.toXMLString(nodeInfo);
//                    Log.d(LOG_TAG, "onAccessibilityEvent: " + xmlString);
                    isCapturing = false;
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return super.onKeyEvent(event);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i(LOG_TAG, "onServiceConnected: 无障碍服务启动成功");
        instance = this;
        layoutInspector = new LayoutInspector(getApplicationContext());
        beanContextHolder.setBean("layoutInspector", layoutInspector);
    }


    /**
     * 判断是否开启无障碍权限
     *
     * @param context
     * @param className 自定义的无障碍服务的类名
     * @return
     */
    public static boolean isServiceON(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
        if (runningServices.size() < 0) {
            return false;
        }
        for (int i = 0; i < runningServices.size(); i++) {
            ComponentName service = runningServices.get(i).service;
            if (service.getClassName().contains(className)) {
                return true;
            }
        }
        return false;
    }
}