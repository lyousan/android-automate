package cn.chci.hmcs.automator.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 有三
 * @Date 2022-07-28 22:12
 * @Description
 **/
public class Node {
    private Node parent;
    private List<Node> children = new ArrayList<>();
    /**
     * 该字段用于在缓存中定位到节点，曲线实现xpath的查找功能，在返回给客户端界面信息时，本地通过dom4j将其转换为Document并缓存起来，
     * 在检索节点时，客户端传递xpath，然后服务端在缓存里查询节点，但是dom4j转换的毕竟是字符格式xml，更实际的节点并不对应，
     * 所以就需要这个字段来将xml中的节点与真实节点关联起来，才能完成后续的诸如点击、输入等交互性操作
     */
    private String cacheId;

    private String id;
    private String className;
    private String text;
    private String contentDesc;
    private boolean checkable;
    private boolean checked;
    private boolean clickable;
    private boolean enabled;
    private boolean focusable;
    private boolean focused;
    private boolean scrollable;
    private boolean longClickable;
    private boolean password;
    private boolean selected;
    private Rectangle rect;

    public Node() {
    }

    public Node(Node parent) {
        this.parent = parent;
    }

    public Node(Node parent, String cacheId) {
        this.parent = parent;
        this.cacheId = cacheId;
    }

    public Node(Node parent, String cacheId, List<Node> children) {
        this.parent = parent;
        this.cacheId = cacheId;
        this.children = children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public String getCacheId() {
        return cacheId;
    }

    public void setCacheId(String cacheId) {
        this.cacheId = cacheId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getContentDesc() {
        return contentDesc;
    }

    public void setContentDesc(String contentDesc) {
        this.contentDesc = contentDesc;
    }

    public boolean isCheckable() {
        return checkable;
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isFocusable() {
        return focusable;
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public boolean isLongClickable() {
        return longClickable;
    }

    public void setLongClickable(boolean longClickable) {
        this.longClickable = longClickable;
    }

    public boolean isPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }
}
