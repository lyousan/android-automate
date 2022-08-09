package cn.chci.hmcs.automate.accessibility.fn;

import android.accessibilityservice.AccessibilityService;

import cn.chci.hmcs.automate.accessibility.clipboard.ClipboardProvider;
import cn.chci.hmcs.automate.model.Command;
import cn.chci.hmcs.automate.utils.AccessibilityServiceGetter;
import cn.chci.hmcs.automate.utils.AccessibilityServiceUtils;
import cn.chci.hmcs.automate.utils.BeanContextHolder;
import cn.chci.hmcs.automate.utils.StringUtils;

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
}
