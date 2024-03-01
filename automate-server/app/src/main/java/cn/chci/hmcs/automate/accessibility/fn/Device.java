package cn.chci.hmcs.automate.accessibility.fn;

import com.google.gson.Gson;

import cn.chci.hmcs.automate.accessibility.device.DeviceInfoProvider;
import cn.chci.hmcs.automate.model.Command;
import cn.chci.hmcs.automate.model.Rect;
import cn.chci.hmcs.automate.utils.BeanContextHolder;

public class Device extends Command {
    private final DeviceInfoProvider deviceInfoProvider;

    public Device() {
        this.deviceInfoProvider = (DeviceInfoProvider) BeanContextHolder.getInstance().getBean("deviceInfoProvider");
    }

    public String getScreenSize() {
        return new Gson().toJson(new Rect(deviceInfoProvider.getDeviceSize()));
    }
}
