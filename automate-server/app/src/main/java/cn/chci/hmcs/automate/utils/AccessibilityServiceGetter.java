package cn.chci.hmcs.automate.utils;

import android.accessibilityservice.AccessibilityService;

import cn.chci.hmcs.automate.MyAccessibilityService;
import cn.chci.hmcs.automate.exception.NoAccessibilityServiceException;

public class AccessibilityServiceGetter {

    public static AccessibilityService getInstance() {
        AccessibilityService instance = MyAccessibilityService.instance;
        if (instance == null) {
            throw new NoAccessibilityServiceException();
        }
        return instance;
    }
}
