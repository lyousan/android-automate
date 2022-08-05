package cn.chci.hmcs.automator.utils;

import android.accessibilityservice.AccessibilityService;

import cn.chci.hmcs.automator.MyAccessibilityService;
import cn.chci.hmcs.automator.accessibility.layout.LayoutInspector;
import cn.chci.hmcs.automator.exception.LayoutInspectorNotInitException;
import cn.chci.hmcs.automator.exception.NoAccessibilityServiceException;

public class AccessibilityServiceGetter {

    public static AccessibilityService getInstance() {
        AccessibilityService instance = MyAccessibilityService.instance;
        if (instance == null) {
            throw new NoAccessibilityServiceException();
        }
        return instance;
    }
}
