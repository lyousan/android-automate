package cn.chci.hmcs.automate.accessibility.activity;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityWindowInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.chci.hmcs.automate.accessibility.AccessibilityEventDelegate;
import cn.chci.hmcs.automate.utils.StringUtils;

public class ActivityInfoProvider implements AccessibilityEventDelegate {
    private static final String LOG_TAG = "ActivityInfoProvider";
    private static String activityName;
    private static String packageName;

    public String currentActivity() {
        return activityName;
    }

    public String currentPackage() {
        return packageName;
    }

    @Override
    public Set<Integer> supportEvents() {
        HashSet<Integer> events = new HashSet<>();
        events.add(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
        return events;
    }

    @Override
    public boolean onAccessibilityEvent(AccessibilityService service, AccessibilityEvent event) {
        List<AccessibilityWindowInfo> windows = service.getWindows();
        AccessibilityWindowInfo windowInfo = windows.stream().filter(window -> window.getId() == event.getWindowId()).findFirst().orElse(null);
        if (windowInfo != null && windowInfo.isFocused()) {
            if (!StringUtils.isEmpty(event.getPackageName())) {
                packageName = event.getPackageName().toString();
            }
            if (!StringUtils.isEmpty(event.getClassName()) && !StringUtils.isEmpty(packageName)) {
                String tmpActivityName = event.getClassName().toString();
                if (tmpActivityName.startsWith(packageName)) {
                    activityName = tmpActivityName.replace(packageName, "");
                }
            }
            Log.d(LOG_TAG, "onAccessibilityEvent: " + packageName + "/" + activityName);
        }
        return false;
    }
}
