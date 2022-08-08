package cn.chci.hmcs.automate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import cn.chci.hmcs.automate.accessibility.device.DisplayDevice;
import cn.chci.hmcs.automate.socket.Server;
import cn.chci.hmcs.automate.utils.AccessibilityServiceUtils;
import cn.chci.hmcs.automate.utils.BeanContextHolder;

public class MainActivity extends AppCompatActivity {
    private BeanContextHolder beanContextHolder = BeanContextHolder.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 提权获取无障碍权限，需要配合adb完成，不过系统存在bug，提权后依旧会跳转到设置界面，而且有的时候会设置失败，可能需要重启手机，这也是安卓系统本身的bug
        try {
            Settings.Secure.putString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, "cn.chci.hmcs.automate/.MyAccessibilityService");
            Settings.Secure.putInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        setAccessibility(getApplicationContext());

        init();
        // 启动socket服务端，端口是33579，客户端（PC）连接前记得连接数据线，然后通过adb进行端口转发
        Server.start();
    }

    private void init() {
        // 显示设备
        beanContextHolder.setBean("displayDevice", new DisplayDevice(this));
        // 无障碍服务工具类
        beanContextHolder.setBean("accessibilityServiceUtils", new AccessibilityServiceUtils(this));
    }

    /**
     * 这是在网上找的免手动设置 开启无障碍的方法，有一些api是系统层面的，在高版本的安卓中已经不开放出来了，反射也调用不了
     *
     * @param context
     */
    @Deprecated
    private void setAccessibility(Context context) {
        try {
//            Class<?> userHandleClazz = Class.forName("android.os.UserHandle");
//            Method myUserId = userHandleClazz.getDeclaredMethod("myUserId");
//            Integer userId = (Integer) myUserId.invoke(userHandleClazz);
//            Set<ComponentName> enabledServices = AccessibilityUtils.getEnabledServicesFromSettings(context);    //这个是获取所有可用的无障碍service列表
            Set<ComponentName> enabledServices = new HashSet<ComponentName>();//这个是获取所有可用的无障碍service列表

            if (enabledServices == (Set<?>) Collections.emptySet()) {    //如果获取的service集合为空,新创建一个集合
                enabledServices = new HashSet<ComponentName>();
            }

            ComponentName toggledService = ComponentName.unflattenFromString("cn.chci.hmcs.automator/.MyAccessibilityService");//添加自己服务的包名和类名
            enabledServices.add(toggledService);    //将需要设置的服务降到集合里
            // Enabling at least one service enables accessibility.

            StringBuilder enabledServicesBuilder = new StringBuilder();

            for (ComponentName enabledService : enabledServices) {
                enabledServicesBuilder.append(enabledService.flattenToString());    //将所有的服务变成字符串,串到一起
                enabledServicesBuilder.append(".");    //每次添加一个服务的时候后面加上分隔符.
            }
            final int enabledServicesBuilderLength = enabledServicesBuilder.length();
            if (enabledServicesBuilderLength > 0) {
                enabledServicesBuilder.deleteCharAt(enabledServicesBuilderLength - 1);    //添加最后一个服务肯定会多出一个分隔符,这里删掉多余的分隔符
            }
            android.provider.Settings.Secure.putString(context.getContentResolver(),
                    android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                    enabledServicesBuilder.toString());    //将服务的字符串重新存储

            // Update accessibility enabled.
            android.provider.Settings.Secure.putInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}