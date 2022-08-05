package cn.chci.hmcs.common.toolkit.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author 杨京三
 * @Date 2021/6/4 16:17
 * @Description
 */
@Slf4j
public class StringUtils {

    /**
     * 查找指定字符串是否存在
     * @param string
     * @param regex
     * @return
     */
    public static boolean findString(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(string);
        return m.find();
    }

    /**
     * 根据正则匹配多个指定字符
     * @param string 源字符串
     * @param regex 正则
     * @return 匹配不到返回 null
     */
    public static List<String> findAllString(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);// 匹配的模式
        Matcher m = pattern.matcher(string);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group(0));
        }
        return list.size() == 0 ? null : list;
    }

    /**
     * 根据正则截取字符串
     *
     * @param string 源字符串
     * @param regex  正则
     * @return 截取不到返回 "" 字符串
     */
    public static String subStringByReg(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);// 匹配的模式
        Matcher m = pattern.matcher(string);
        while (m.find()) {
            return m.group(0);
        }
        return "";
    }

    /**
     * 根据正则截取字符串
     *
     * @param string 源字符串
     * @param pattern  正则
     * @return 截取不到返回 "" 字符串
     */
    public static String subStringByReg(String string, Pattern pattern) {
        Matcher m = pattern.matcher(string);
        if (m.find()) {
            return m.group(m.groupCount());
        }
        return "";
    }

    /**
     * 判断非空
     *
     * @param string 源字符串
     * @param wish   为空时希望返回的数据
     * @return 不为空时返回源字符串，为空时返回预定的字符串
     */
    public static String assertNonBlank(String string, String wish) {
        if (!isEmpty(string)) {
            return string;
        }
        return wish;
    }


    /**
     * 从url中获取指定参数
     *
     * @param url url
     * @param key 参数名称
     * @return
     */
    public static String getQuery(String url, String key) {
        url = url.substring(url.indexOf("?") + 1);
        String[] kvs = url.split("&");
        for (String kv : kvs) {
            String[] param = kv.split("=");
            if (param[0].equals(key)) {
                return param[1];
            }
        }
        return "";
    }

    /**
     * 字符串判空，包括null和空字符串，但不包括空格，制表符之类的特殊字符
     *
     * @param str
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return str.length() == 0 || str.equals("");
    }
}
