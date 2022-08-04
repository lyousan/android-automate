package cn.chci.hmcs.automator.model;

import android.content.res.Resources;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 对界面节点信息的包装，便于遍历、转换
 */
public class Node {
    /**
     * 真实的节点信息
     */
    private final AccessibilityNodeInfo node;
    /**
     * 从autojs里扣过来的，暂时不知道可以干什么，但是应该有用
     */
    private final Resources resources;
    private final Node parent;
    private List<Node> children = new ArrayList<>();
    /**
     * 该字段用于在虚拟节点中定位，变相实现xpath的查找功能，在获取界面节点信息时，将真实节点信息转换为字符串格式的xml，再通过dom4j转换为虚拟节点（{@link org.dom4j.Document}对象）；
     * 在检索节点时，客户端传递xpath，然后服务端在虚拟节点中查询，但是虚拟节点无法进行点击、输入等交互性操作，所以就需要这个字段来将xml中的节点与真实节点关联起来，才能完成后续的诸如点击、输入等交互性操作。
     * <p>
     * 该值实际为{@link AccessibilityNodeInfo#hashCode()}值，使用该值的好处是在同一个界面中，节点的hashCode是固定的，界面改变后hashCode也会随之改变，
     * 非常适合用来判断节点是否有效，客户端可能会传递一个已经过期的节点cacheId过来，所以在点击、输入等交互性操作时须要先判断节点是否有效。
     */
    private final String cacheId;

    public Node(AccessibilityNodeInfo node, Resources resources, Node parent, String cacheId) {
        this.resources = resources;
        this.node = node;
        this.parent = parent;
        this.cacheId = cacheId;
    }

    public AccessibilityNodeInfo getNode() {
        return node;
    }

    public Resources getResources() {
        return resources;
    }

    public Node getParent() {
        return parent;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node1 = (Node) o;
        // 这里实际上比较的AccessibilityNodeInfo的hashCode跟equals方法
        return Objects.equals(cacheId, node1.cacheId) &&
                Objects.equals(node, node1.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cacheId);
    }
}
