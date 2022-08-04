package cn.chci.hmcs.automator.model;

import cn.chci.hmcs.automator.fn.NodeActions;
import cn.chci.hmcs.automator.socket.Client;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 有三
 * @Date 2022-07-28 22:12
 * @Description
 **/
public class Node {
    /**
     * 这个客户端的引用是用来实现一些节点相关的操作，如click，input等
     */
    private Client client;

    private NodeActions nodeActions;
    /**
     * 通过find和findOne系列方法得到Node都没有parent，因为服务端在xpath的实现上存在着问题，实际上只返回了这个节点及其子孙节点，
     * 后期如果有需要的话可以做一个懒加载，需要访问parent的时候去重新获取
     */
    private Node parent;
    private List<Node> children = new ArrayList<>();
    /**
     * 该字段用于在虚拟节点中定位，变相实现xpath的查找功能，在获取界面节点信息时，将真实节点信息转换为字符串格式的xml，再通过dom4j转换为虚拟节点（{@link org.dom4j.Document}对象）；
     * 在检索节点时，客户端传递xpath，然后服务端在虚拟节点中查询，但是虚拟节点无法进行点击、输入等交互性操作，所以就需要这个字段来将xml中的节点与真实节点关联起来，才能完成后续的诸如点击、输入等交互性操作。
     * <p>
     * 该值实际为真实节点的hash值，使用该值的好处是在同一个界面中，节点的hashCode是固定的，界面改变后hashCode也会随之改变，
     * 非常适合用来判断节点是否有效，客户端可能会传递一个已经过期的节点cacheId过来，所以在点击、输入等交互性操作时须要先判断节点是否有效。
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

    /////////////////////////////////////////////

    public boolean click() {
        if (nodeActions == null) {
            nodeActions = new NodeActions();
        }
        return nodeActions.click(client, this);
    }

    public boolean longClick() {
        if (nodeActions == null) {
            nodeActions = new NodeActions();
        }
        return nodeActions.longClick(client, this);
    }

    public boolean input(String text) {
        if (nodeActions == null) {
            nodeActions = new NodeActions();
        }
        return nodeActions.input(client, this, text);
    }

    /////////////////////////////////////////////

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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
