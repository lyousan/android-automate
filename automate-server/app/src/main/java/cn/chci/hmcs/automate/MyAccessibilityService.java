package cn.chci.hmcs.automate;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import cn.chci.hmcs.automate.accessibility.AccessibilityEventDelegate;
import cn.chci.hmcs.automate.accessibility.layout.LayoutInspector;
import cn.chci.hmcs.automate.utils.BeanContextHolder;

import java.util.ArrayList;
import java.util.List;

public class MyAccessibilityService extends AccessibilityService {
    private static final String LOG_TAG = "MyAccessibilityService";
    @SuppressLint("StaticFieldLeak")
    public static AccessibilityService instance;
    private final BeanContextHolder beanContextHolder = BeanContextHolder.getInstance();
    public LayoutInspector layoutInspector;
    private static volatile boolean isCapturing;
    public static List<AccessibilityEventDelegate> delegates = new ArrayList<>();

    public MyAccessibilityService() {
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // TODO 监听一些事件，暂时没啥用，因为我们才是主动调用方
//        Log.i(LOG_TAG, "onAccessibilityEvent: 接收到事件");
        for (AccessibilityEventDelegate delegate : delegates) {
            if (delegate.supportEvents() != null && delegate.supportEvents().contains(event.getEventType())) {
                if (!delegate.onAccessibilityEvent(this, event)) {
                    break;
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
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
            if (runningServices.isEmpty()) {
                return false;
            }
            for (int i = 0; i < runningServices.size(); i++) {
                ComponentName service = runningServices.get(i).service;
                if (service.getClassName().contains(className)) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "isServiceON error: ",e );
        }
        return false;
    }
}