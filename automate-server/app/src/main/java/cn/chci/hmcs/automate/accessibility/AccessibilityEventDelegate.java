package cn.chci.hmcs.automate.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import java.util.Set;

public interface AccessibilityEventDelegate {
    Set<Integer> supportEvents();

    boolean onAccessibilityEvent(AccessibilityService service, AccessibilityEvent event);
}
