package cn.chci.hmcs.automate.utils;

public class StringUtils {
    /**
     * 字符串判空，包括null和空字符串，但不包括空格，制表符之类的特殊字符
     *
     * @param str
     * @return 是否为空
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null) {
            return true;
        }
        return str.length() == 0;
    }
}
