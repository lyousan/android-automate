package cn.chci.hmcs.automate.utils;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;

public class AccessibilityServiceUtils {
    private Activity mActivity;

    public AccessibilityServiceUtils(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void gotoAccessibilitySetting() {
        // 判断当前是否开启了无障碍权限，如果没有的话就会跳转到无障碍的设置界面
//        if (!MyAccessibilityService.isServiceON(mActivity.getApplicationContext(), MyAccessibilityService.class.getName())) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
//        }
    }
}
