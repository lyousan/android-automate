package cn.chci.hmcs.automator.accessibility.fn;

import android.accessibilityservice.AccessibilityService;

import cn.chci.hmcs.automator.model.Command;
import cn.chci.hmcs.automator.utils.AccessibilityServiceGetter;
import cn.chci.hmcs.automator.utils.AccessibilityServiceUtils;
import cn.chci.hmcs.automator.utils.BeanContextHolder;

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
}
