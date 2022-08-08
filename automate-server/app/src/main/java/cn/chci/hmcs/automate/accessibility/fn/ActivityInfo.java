package cn.chci.hmcs.automate.accessibility.fn;

import cn.chci.hmcs.automate.accessibility.activity.ActivityInfoProvider;
import cn.chci.hmcs.automate.model.Command;
import cn.chci.hmcs.automate.utils.BeanContextHolder;

public class ActivityInfo extends Command {
    private final ActivityInfoProvider provider;

    public ActivityInfo() {
        provider = (ActivityInfoProvider) BeanContextHolder.getInstance().getBean("activityInfoProvider");
    }

    public String currentActivity() {
        return provider.currentActivity();
    }

    public String currentPackage()   {
        return provider.currentPackage();
    }
}
