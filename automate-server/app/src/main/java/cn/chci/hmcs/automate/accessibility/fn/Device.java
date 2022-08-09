package cn.chci.hmcs.automate.accessibility.fn;

import com.alibaba.fastjson2.JSON;

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
        return JSON.toJSONString(new Rect(deviceInfoProvider.getDeviceSize()));
    }
}
