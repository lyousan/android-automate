# 项目描述

该项目是基于安卓无障碍服务开发的安卓自动化中间件，用于进行UI自动化的数据采集。
项目的灵感和无障碍服务的使用借鉴于开源项目`AutoJs`，不同的是该项目采用`客户端与服务端`的架构方式，安卓手机作为服务端，PC电脑作为客户端，双方通过socket进行连接。

---

# 模块说明

## AutomatorServer

该模块为安卓工程，使用SDK安卓10(API30)开发，主要实现无障碍服务的细化功能封装并暴露给客户端，建议使用`Android Studio`打开项目。

## AutomatorClient

该模块为运行在PC端的客户端程序，主要进行一个示例演示和功能调试

---

# 使用说明

`AutomatorClient`模块下的`resources/automator.apk`是一个已经打包编译并签名的apk，可以直接安装到手机上。
连接上数据线，开启usb调试模式后，将`resources/config.properties`中的`udid`的值改为当前设备的`udid`，最后启动`cn.chci.hmcs.automator.socket.Main#main`即可。
必须要先安装上apk后再启动客户端程序，如果启动后手机跳转到无障碍的设置界面，但是并没有显示出`Automator`的话，需要清理后台手动开关几次或者重启。
客户端程序启动成功后，在控制台输入任意字符即可进行测试。
> 输入 `dump` 会返回手机当前的界面信息，如果没有正常获取到无障碍权限的话会提示 “layoutInspector暂未初始化”

---
# 开发计划
- 协议制定与实现
- 控件操作（点击、输入......）
- API封装
- ......

---

# 未完待续 ......