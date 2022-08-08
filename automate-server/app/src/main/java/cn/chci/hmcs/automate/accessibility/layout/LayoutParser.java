package cn.chci.hmcs.automate.accessibility.layout;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import cn.chci.hmcs.automate.model.Node;

public class LayoutParser {

    /**
     * 将节点信息转换为字符串格式的xml
     *
     * @param node 节点信息
     * @return 字符串格式的xml
     */
    public static String toXMLString(Node node) {
        return toXMLString(node, true);
    }

    /**
     * 将节点信息转换为字符串格式的xml
     *
     * @param node 节点信息
     * @param full 是否完整，为true会递归转换所有节点，为false则只转换当前节点本身
     * @return 字符串格式的xml
     */
    public static String toXMLString(Node node, boolean full) {
        if (node == null) {
            return "";
        }
        if (full) {
            StringBuilder result = new StringBuilder();
            result.append("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>");
            result.append(convertToXMLString(node, result));
            return result.toString();
        }
        return convertSingleToXMLString(node);
    }

    private static String convertSingleToXMLString(Node nodeInfo) {
        StringBuilder result = new StringBuilder("<node");
        populateAttrs(nodeInfo, result);
        result.append("/>");
        return result.toString();
    }

    /**
     * 通过递归将节点一层一层都转换为字符串的xml然后拼接在一起
     *
     * @param nodeInfo 节点信息
     * @param builder  用于字符串拼接
     * @return 返回传入节点的字符串XML格式
     */
    private static String convertToXMLString(Node nodeInfo, StringBuilder builder) {
        StringBuilder result = new StringBuilder();
        // 不用className作为tag的原因是因为有一些app自定义的className中存在一些诸如$之类的不符合Xml标签规范的字符，会导致解析出现异常
        result.append("<node");
        // 填充节点的属性
        populateAttrs(nodeInfo, result);
        List<Node> children = nodeInfo.getChildren();
        if (children.size() > 0) {
            // 存在子节点就继续递归转换
            result.append(">");
            StringBuilder childrenString = new StringBuilder();
            for (Node child : children) {
                childrenString.append(convertToXMLString(child, builder));
            }
            result.append(childrenString);
            result.append("</node>");
        } else {
            // 没有子节点就直接封尾，用 <tag /> 这种形式，而不是 <tag></tag> 这种形式，前者更加节省空间
            result.append("/>");
        }
        return result.toString();
    }

    /**
     * 填充属性
     *
     * @param nodeInfo 节点信息
     * @param builder  字用于字符串拼接
     */
    private static void populateAttrs(Node nodeInfo, StringBuilder builder) {
        Rect rect = new Rect();
        AccessibilityNodeInfo node = nodeInfo.getNode();
        node.getBoundsInScreen(rect);
        builder.append(" ").append("resource-id=\"").append(node.getViewIdResourceName() == null ? "" : replaceInvalidCharacter(node.getViewIdResourceName())).append("\"")
                .append(" ").append("class=\"").append(replaceInvalidCharacter(node.getClassName().toString())).append("\"")
                .append(" ").append("text=\"").append(node.getText() == null ? "" : replaceInvalidCharacter(node.getText().toString())).append("\"")
                .append(" ").append("content-desc=\"").append(node.getContentDescription() == null ? "" : replaceInvalidCharacter(node.getContentDescription().toString())).append("\"")
                .append(" ").append("checkable=\"").append(node.isCheckable()).append("\"")
                .append(" ").append("checked=\"").append(node.isChecked()).append("\"")
                .append(" ").append("clickable=\"").append(node.isClickable()).append("\"")
                .append(" ").append("enabled=\"").append(node.isEnabled()).append("\"")
                .append(" ").append("focusable=\"").append(node.isFocusable()).append("\"")
                .append(" ").append("focused=\"").append(node.isFocused()).append("\"")
                .append(" ").append("scrollable=\"").append(node.isScrollable()).append("\"")
                .append(" ").append("long-clickable=\"").append(node.isLongClickable()).append("\"")
                .append(" ").append("password=\"").append(node.isPassword()).append("\"")
                .append(" ").append("selected=\"").append(node.isSelected()).append("\"")
                .append(" ").append("bounds=\"").append(rect.toShortString()).append("\"")
                // 注意：这个cacheId并不是原生存在的，是用来辅助服务端实现xpath定位能力的，用来跟真实节点关联的，不能用于xpath表达式中
                .append(" ").append("cacheId=\"").append(nodeInfo.getCacheId()).append("\"");
    }

    private static String replaceInvalidCharacter(String content) {
        return content.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("&", "&amp;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&apos;");
    }
}
