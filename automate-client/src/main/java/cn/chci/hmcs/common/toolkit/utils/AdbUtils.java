package cn.chci.hmcs.common.toolkit.utils;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author 杨京三
 * @Date 2021/12/20 9:49
 * @Description
 */
@Slf4j
public class AdbUtils {
    private static final String GREP_OR_FINDSTR;
    private static final String OS_NAME;
    private static final String EXECUTOR;
    private static final String EXECUTOR_PARAM;
    private static final String LINUX = "Linux";
    private static final String WINDOWS = "Windows";

    static {
        OS_NAME = System.getProperty("os.name");
        if (OS_NAME.contains(LINUX)) {
            EXECUTOR = "/bin/bash";
            EXECUTOR_PARAM = "-c";
            GREP_OR_FINDSTR = "grep";
        } else if (OS_NAME.contains(WINDOWS)) {
            EXECUTOR = "cmd.exe";
            EXECUTOR_PARAM = "/c";
            GREP_OR_FINDSTR = "findstr";
        } else {
            EXECUTOR = "/bin/sh";
            EXECUTOR_PARAM = "-c";
            GREP_OR_FINDSTR = "grep";
        }
    }

    /**
     * 卸载移动设备中appium相关组件
     *
     * @param udid
     */
    public static void uninstallAppium(String udid) {
        try {
            exec("adb -s " + udid + " shell pm uninstall io.appium.uiautomator2.server.test");
            exec("adb -s " + udid + " shell pm uninstall io.appium.uiautomator2.server");
            exec("adb -s " + udid + " shell pm uninstall io.appium.settings");
        } catch (IOException | InterruptedException e) {
            log.error("卸载移动设备中appium相关组件时发生异常：", e);
        }
    }

    /**
     * 关闭ADB服务
     * 这是一个危险程度很高操作，关闭adb时会清空掉所有的转发接口，会导致所有正在运行appium的设备统统报错
     * 但是这个方法配合uninstallAppium(String udid)方法，的确能够快速而又干净的清理adb和appium的运行环境
     * <p>
     * 开发事故：
     * 中途加入设备，导致其他设备出现报错的罪魁祸首就是这个方法，一开始在每个设备执行前调用此方法是为了配合uninstallAppium(String udid)方法
     * 来清空设备的运行环境，以保证之前未正常结束的连接不会影响到设备的正常运行，这个bug困扰了我3天( ╯□╰ )，翻阅无数资料，追溯appium源
     * 码，反复分析日志，最后却是我庸人自扰，画蛇添足了orz
     */
    public static void stopADB() {
        try {
            Process process = Runtime.getRuntime().exec("adb kill-server");
            process.waitFor(20, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e) {
            log.error("关闭ADB服务时发生异常：", e);
        }
    }

    /**
     * 开启ADB服务
     * 这个方法使用场景很少，appium会自动启动adb服务，在appium的源码里，ADB类下的createADB()函数里就调用了adb start-server
     */
    public static void startADB() {
        try {
            Process process = Runtime.getRuntime().exec("adb start-server");
            process.waitFor(20, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e) {
            log.error("开启ADB服务时发生异常：", e);
        }
    }

    /**
     * 查询包
     *
     * @param packageName 包名
     * @return 与传入包名相关的包
     */
    public static List<String> listPackages(String packageName) {
        try {
            return exec("adb shell pm list packages | " + GREP_OR_FINDSTR + packageName);
        } catch (IOException | InterruptedException e) {
            log.error("查询 [{}] 包时出现异常：{}", packageName, e);
        }
        return null;
    }

    /**
     * 查询包
     *
     * @param packageName 包名
     * @param udid        设备序列号
     * @return 与传入包名相关的包
     */
    public static List<String> listPackages(String packageName, String udid) {
        try {
            return exec("adb -s " + udid + " shell pm list packages | " + GREP_OR_FINDSTR + packageName);
        } catch (IOException | InterruptedException e) {
            log.error("查询 [{}] 包时出现异常：{}", packageName, e);
        }
        return null;
    }

    /**
     * 安装包
     *
     * @param file apk文件
     */
    public static void installPackage(File file) {
        try {
            exec("adb install \"" + file.getAbsolutePath() + "\"");
        } catch (IOException | InterruptedException e) {
            log.error("安装 [{}] 包时出现异常：{}", file.getAbsolutePath(), e);
        }
    }

    /**
     * 安装包
     *
     * @param file apk文件
     * @param udid 设备序列号
     */
    public static void installPackage(File file, String udid) {
        try {
            String command = "adb -s " + udid + " install \"" + file.getAbsolutePath() + "\"";
            exec(command);
        } catch (IOException | InterruptedException e) {
            log.error("安装 [{}] 包时出现异常：{}", file.getAbsolutePath(), e);
        }
    }

    /**
     * 设置输入法
     *
     * @param imePackageName 输入法包名
     *                       请先通过{@link #listPackages(String)}方法确认设备中存在该输入法
     */
    public static void setInputMethod(String imePackageName) {
        try {
            exec("adb shell settings put secure default_input_method " + imePackageName);
        } catch (IOException | InterruptedException e) {
            log.error("设置输入法时发生异常：{}", e);
        }
    }

    /**
     * @param imePackageName 输入法包名
     *                       请先通过{@link #listPackages(String)}方法确认设备中存在该输入法
     * @param udid           设备序列号
     */
    public static void setInputMethod(String imePackageName, String udid) {
        try {
            exec("adb -s " + udid + " shell settings put secure default_input_method " + imePackageName);
        } catch (IOException | InterruptedException e) {
            log.error("设置输入法时发生异常：{}", e);
        }
    }

    /**
     * 获取当前输入法
     *
     * @param udid 设备序列号
     */
    public static String getInputMethod(String udid) {
        try {
            List<String> res = exec("adb -s " + udid + " shell settings get secure default_input_method");
            return res.isEmpty() ? null : res.get(0);
        } catch (IOException | InterruptedException e) {
            log.error("获取输入法时发生异常：{}", e);
        }
        return null;
    }

    /**
     * 设置adb输入法
     * 传入udid
     * 成功返回true，失败返回false
     * ---- adb -s udid shell ime set com.android.adbkeyboard/.AdbIME
     * ---- adb -s udid shell settings put secure default_input_method <packagename>
     *
     * @Link:https://github.com/senzhk/ADBKeyBoard
     */
    public static boolean setADBKeyboard(String udid) {
        try {
            List<String> execResult = AdbUtils.exec("adb -s " + udid + " shell ime set com.android.adbkeyboard/.AdbIME");
            log.debug(execResult.toString());
            return true;
        } catch (IOException | InterruptedException e) {
            log.error("设置adb输入法失败:{}" + e.toString());
            return false;
        }
    }

    /**
     * 通过ADBKeyboard输入文本,返回布尔值
     */
    public static boolean inputTextByADBKeyboard(String udid, String text) {
        List<String> result = null;
        try {
            result = AdbUtils.exec("adb -s " + udid + " shell am broadcast -a ADB_INPUT_TEXT --es msg " + text);
            log.debug(result.toString());
            return true;
        } catch (IOException | InterruptedException e) {
            log.error("通过ADBKeyboard输入失败：{}", e.toString());
            return false;
        }
    }

    /**
     * 设置讯飞输入法
     */
    public static void setXunFeiKeyboard(String udid) {
        // 设置讯飞输入法
        AdbUtils.setInputMethod("com.iflytek.inputmethod/.FlyIME", udid);
    }


    /**
     * 通过uiautomator dump当前布局
     *
     * @param file 保存路径
     * @return 执行是否成功
     */
    public static boolean uiautomatorDump(File file) {
        try {
            List<String> result = exec("adb shell uiautomator dump");
            if (result.contains("UI hierchary dumped to: /sdcard/window_dump.xml")) {
                exec("adb pull /sdcard/window_dump.xml \"" + file.getAbsolutePath() + "\"");
                return true;
            }
        } catch (IOException | InterruptedException e) {
            log.error("uiauomator dump时出现异常");
        }
        return false;
    }

    /**
     * 通过uiautomator dump当前布局
     *
     * @param file 保存路径
     * @param udid 设备序列号
     * @return 执行是否成功
     */
    public static boolean uiautomatorDump(File file, String udid) {
        try {
            List<String> result = exec("adb -s " + udid + " shell uiautomator dump");
            if (result.contains("UI hierchary dumped to: /sdcard/window_dump.xml")) {
                exec("adb -s " + udid + " pull /sdcard/window_dump.xml \"" + file.getAbsolutePath() + "\"");
                return true;
            }
        } catch (IOException | InterruptedException e) {
            log.error("uiauomator dump时出现异常");
        }
        return false;
    }

    /**
     * @param: [dumpFile, udid]
     * @return: org.dom4j.Document
     * @description: 保存xml文件的路径, 手机udid
     * 获取页面源码对应的Document,如果获取失败返回null。
     */
    public static Document getPageSourceDocument(File dumpFile, String udid) {
        try {
            SAXReader reader = new SAXReader();
            // 保存手机xml布局
            AdbUtils.uiautomatorDump(dumpFile, udid);
            FileInputStream fis = new FileInputStream(dumpFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = fis.read(b, 0, b.length)) != -1) {
                baos.write(b, 0, len);
            }
            byte[] buffer = baos.toByteArray();
            return reader.read(new ByteArrayInputStream(buffer));
        } catch (IOException | DocumentException e) {
            return null;
        }
    }

    /**
     * @param: [udid]
     * @return: int[]
     * @description: 通过adb获取手机设备的屏幕大小
     */
    public static int[] getMobileDevicePhysicalSize(String udid) {
        try {
            List<String> consoleResult = AdbUtils.exec("adb -s " + udid + " shell wm size");
            log.debug("获取手机屏幕大小，控制台输出" + consoleResult.toString());
            for (String s : consoleResult) {
                String trimString = s.trim();
                if (trimString.startsWith("Physical size:")) {
                    String si = trimString.substring(s.indexOf(":") + 1);
                    String siz = si.trim();
                    int width = Integer.parseInt(siz.split("x")[0]);
                    int height = Integer.parseInt(siz.split("x")[1]);
                    int[] size = new int[2];
                    size[0] = width;
                    size[1] = height;
                    log.debug("匹配尺寸：{" + size[0] + "," + size[1] + "}");
                    return size;
                }
            }
            return null;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @param: [file, config]
     * @return: boolean
     * @description: 获取剪切板数据(需要再手机上提前安装https : / / github.com / majido / clipper)
     * # 用法
     * # am broadcast -a clipper.set -e text "this can be pasted now"
     * # am broadcast -a clipper.get
     */
    private static String getTextFromClipboardServie(String udid) {
        try {
            List<String> data = AdbUtils.exec("adb -s " + udid + " shell am broadcast -a clipper.get");
            for (String s : data) {
                if (s.trim().contains("data=")) {
                    return (s.substring(s.indexOf("data=") + 6, s.length() - 1));
                }
            }
        } catch (IOException | InterruptedException e) {
            log.debug("获取剪贴板数据异常：" + e);
        }
        return "";
    }


    /**
     * 获取当前设备的系统版本
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static String getSystemVersion() throws IOException, InterruptedException {
        List<String> result = exec("adb -s shell getprop ro.build.version.release");
        return result.size() > 0 ? result.get(0).trim() : null;
    }

    /**
     * 获取指定设备的系统版本
     *
     * @param udid 设备序列号
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static String getSystemVersion(String udid) throws IOException, InterruptedException {
        List<String> result = exec("adb -s " + udid + " shell getprop ro.build.version.release");
        return result.size() > 0 ? result.get(0).trim() : null;
    }

    /**
     * 获取指定包名app的版本名称
     *
     * @param packageName app完整包名
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static String getAppVersion(String packageName) throws IOException, InterruptedException {
        List<String> result = exec("adb shell pm dump " + packageName + " | " + GREP_OR_FINDSTR + " version");
        Object[] versionNames = result.stream().filter(line -> line.contains("versionName")).toArray();
        return versionNames.length > 0 ? versionNames[0].toString().replace(" versionName=", "") : null;
    }

    /**
     * 获取指定设备设备中指定包名app的版本名称
     *
     * @param packageName app完整包名
     * @param udid        设备序列号
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static String getAppVersion(String packageName, String udid) throws IOException, InterruptedException {
        List<String> result = exec("adb -s " + udid + " shell pm dump " + packageName + " | " + GREP_OR_FINDSTR + " version");
        log.debug("exec result ==> {}", result);
        Object[] versionNames = result.stream().filter(line -> line.contains("versionName")).toArray();
        return versionNames.length > 0 ? versionNames[0].toString().replace(" versionName=", "").trim() : null;
    }

    /**
     * 获取当前设备无线网卡的ip地址
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static String getIpInWlan() throws IOException, InterruptedException {
        List<String> result = exec("adb shell ip addr show wlan0");
        StringBuilder builder = new StringBuilder();
        result.stream().forEach(line -> builder.append(line));
        String pattern = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(builder.toString());
        if (m.find()) {
            return m.group(0);
        }
        return null;
    }

    /**
     * 获取当前设备无线网卡的ip地址
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static String getIpInWlan(String udid) throws IOException, InterruptedException {
        List<String> result = exec("adb -s " + udid + " shell ip addr show wlan0");
        StringBuilder builder = new StringBuilder();
        result.stream().forEach(builder::append);
        String pattern = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(builder.toString());
        if (m.find()) {
            return m.group(0);
        }
        return null;
    }

    /**
     * 执行cmd命令
     *
     * @param command cmd 命令
     * @return 返回结果
     * @throws IOException
     */
    public static List<String> exec(String command) throws IOException, InterruptedException {
        log.debug("exec command ==> {}", command);
        List<String> list = new ArrayList<>();
        ProcessBuilder pb = new ProcessBuilder(EXECUTOR, EXECUTOR_PARAM, command);
        pb.redirectErrorStream(true);
        Process process = pb.start();
//        Process process = Runtime.getRuntime().exec(new String[]{EXECUTOR, EXECUTOR_PARAM, command});  当adb本身有一些问题时（连接不稳定、连接超时、连接被拒绝等等情况），有可能会导致长时间的阻塞
        process.waitFor(20, TimeUnit.SECONDS);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String len = null;
        while ((len = reader.readLine()) != null) {
            list.add(len);
        }
        reader.close();
        return list;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 点击屏幕
     *
     * @param x x坐标
     * @param y y坐标
     */
    public static void tap(int x, int y) {
        try {
            exec("adb shell input tap " + x + " " + y);
        } catch (IOException | InterruptedException e) {
            log.error("点击屏幕时发生异常：", e);
        }
    }

    /**
     * 点击屏幕
     *
     * @param udid 设备序列号
     * @param x    x坐标
     * @param y    y坐标
     */
    public static void tap(String udid, int x, int y) {
        try {
            exec("adb -s " + udid + " shell input tap " + x + " " + y);
        } catch (IOException | InterruptedException e) {
            log.error("点击屏幕时发生异常：", e);
        }
    }

    /**
     * 点击屏幕
     *
     * @param coord 坐标
     */
    public static void tap(int[] coord) {
        if (coord == null || coord.length < 2) {
            return;
        }
        try {
            exec("adb -s shell input tap " + coord[0] + " " + coord[1]);
        } catch (IOException | InterruptedException e) {
            log.error("点击屏幕时发生异常：", e);
        }
    }

    /**
     * 点击屏幕
     *
     * @param udid  设备序列号
     * @param coord 坐标
     */
    public static void tap(String udid, int[] coord) {
        if (coord == null || coord.length < 2) {
            return;
        }
        try {
            exec("adb -s " + udid + " shell input tap " + coord[0] + " " + coord[1]);
        } catch (IOException | InterruptedException e) {
            log.error("点击屏幕时发生异常：", e);
        }
    }

    /**
     * 滑动屏幕
     *
     * @param x1       起点x
     * @param y1       起点y
     * @param x2       终点x
     * @param y2       终点y
     * @param duration 持续时间
     */
    public static void swipe(int x1, int y1, int x2, int y2, long duration) {
        try {
            exec("adb shell input swipe " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + duration);
        } catch (IOException | InterruptedException e) {
            log.error("滑动屏幕时发生异常：", e);
        }
    }

    /**
     * 滑动屏幕
     *
     * @param udid     设备序列号
     * @param x1       起点x
     * @param y1       起点y
     * @param x2       终点x
     * @param y2       终点y
     * @param duration 持续时间
     */
    public static void swipe(String udid, int x1, int y1, int x2, int y2, long duration) {
        try {
            exec("adb -s " + udid + " shell input swipe " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + duration);
        } catch (IOException | InterruptedException e) {
            log.error("滑动屏幕时发生异常：", e);
        }
    }

    /**
     * @param: [udid, start, end, duriation]
     * @return: void
     * @description: 通过adb滑动屏幕
     */
    public static boolean swipe(String udid, int[] start, int[] end, long duriation) {
        try {
            AdbUtils.exec("adb -s " + udid + " shell input swipe " + start[0] + " " + start[1] + " " + end[0] + " " + end[1] + " " + duriation);
            return true;
        } catch (IOException | InterruptedException e) {
            log.error("滑动屏幕时发生异常：{}", e);
            return false;
        }
    }

    /**
     * 截图并dump当前布局
     *
     * @param dir 存放的目录
     * @return 文件名，图片和布局文件以后缀区别
     */
    public static String screenshotAndDumpLayout(File dir) {
        String fileName = System.currentTimeMillis() + "";
        // 截图
        try {
            exec("adb shell screencap -p /sdcard/screencap.png");
            exec("adb pull /sdcard/screencap.png " + new File(dir, fileName + ".png").getAbsolutePath());
        } catch (IOException | InterruptedException e) {
            log.error("截图并dump当前布局时发生异常: ", e);
        }
        // 布局
        uiautomatorDump(new File(dir, fileName + ".xml"));
        String dump = dir.getAbsolutePath() + File.separator + fileName;
        log.debug("dump file: {}", dump);
        return dump;
    }

    /**
     * 截图并dump当前布局
     *
     * @param dir  存放的目录
     * @param udid 设备序列号
     * @return 文件名，图片和布局文件以后缀区别
     */
    public static String screenshotAndDumpLayout(File dir, String udid) {
        FileUtils.checkDirectory(dir, true);
        String fileName = System.currentTimeMillis() + "";
        // 截图
        try {
            exec("adb -s " + udid + " shell screencap -p /sdcard/screencap.png");
            exec("adb -s " + udid + " pull /sdcard/screencap.png " + new File(dir, fileName + ".png").getAbsolutePath());
        } catch (IOException | InterruptedException e) {
            log.error("截图并dump当前布局时发生异常: ", e);
        }
        // 布局
        uiautomatorDump(new File(dir, fileName + ".xml"), udid);
        String dump = dir.getAbsolutePath() + File.separator + fileName;
        log.debug("dump file: {}", dump);
        return dump;
    }


}
