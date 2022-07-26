package cn.chci.hmcs.automator;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class NodeInfoParser {

    /**
     * 将节点信息转换为字符串格式的xml
     *
     * @param nodeInfo 节点信息
     * @return 字符串格式的xml
     */
    public static String toXMLString(NodeInfo nodeInfo) {
        StringBuilder result = new StringBuilder();
        result.append("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>");
        result.append(convertToXMLString(nodeInfo, result));
        return result.toString();
    }

    /**
     * 通过递归将节点一层一层都转换为字符串的xml然后拼接在一起
     *
     * @param nodeInfo 节点信息
     * @param builder  用于字符串拼接
     * @return
     */
    private static String convertToXMLString(NodeInfo nodeInfo, StringBuilder builder) {
        StringBuilder result = new StringBuilder();
        AccessibilityNodeInfo node = nodeInfo.getNode();
        CharSequence className = node.getClassName();
        result.append("<").append(className);
        // 填充节点的属性
        populateAttrs(node, result);
        List<NodeInfo> children = nodeInfo.getChildren();
        if (children.size() > 0) {
            // 存在子节点就继续递归转换
            result.append(">");
            StringBuilder childrenString = new StringBuilder();
            for (NodeInfo child : children) {
                childrenString.append(convertToXMLString(child, builder));
            }
            result.append(childrenString);
            result.append("</").append(className).append(">");
        } else {
            // 没有子节点就直接封尾，用 <tag /> 这种形式，而不是 <tag></tag> 这种形式，前者更加节省空间
            result.append("/>");
        }
        return result.toString();
    }

    /**
     * 填充属性
     *
     * @param node    节点
     * @param builder 字用于字符串拼接
     */
    private static void populateAttrs(AccessibilityNodeInfo node, StringBuilder builder) {
        Rect rect = new Rect();
        node.getBoundsInScreen(rect);
        builder.append(" ").append("resource-id=\"").append(node.getViewIdResourceName() == null ? "" : node.getViewIdResourceName()).append("\"")
                .append(" ").append("class=\"").append(node.getClassName()).append("\"")
                .append(" ").append("text=\"").append(node.getText() == null ? "" : node.getText()).append("\"")
                .append(" ").append("content-desc=\"").append(node.getContentDescription() == null ? "" : node.getContentDescription()).append("\"")
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
                .append(" ").append("bounds=\"").append(rect.toShortString()).append("\"");
    }
}
