package cn.chci.hmcs.automator.model;

import android.content.res.Resources;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
     * 该字段用于在缓存中定位到节点，曲线实现xpath的查找功能，在返回给客户端界面信息时，本地通过dom4j将其转换为Document并缓存起来，
     * 在检索节点时，客户端传递xpath，然后服务端在缓存里查询节点，但是dom4j转换的毕竟是字符格式xml，更实际的节点并不对应，
     * 所以就需要这个字段来将xml中的节点与真实节点关联起来，才能完成后续的诸如点击、输入等交互性操作
     */
    private final String cacheId;

    public Node(AccessibilityNodeInfo node, Resources resources, Node parent) {
        this.resources = resources;
        this.node = node;
        this.parent = parent;
        this.cacheId = UUID.randomUUID().toString();
    }

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

}
