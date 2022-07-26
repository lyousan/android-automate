package cn.chci.hmcs.automator;

import android.content.res.Resources;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 对界面节点信息的包装，便于遍历、转换
 */
public class NodeInfo {
    /**
     * 真实的节点信息
     */
    private AccessibilityNodeInfo node;
    /**
     * 从autojs里扣过来的，暂时不知道可以干什么，但是应该有用
     */
    private Resources resources;
    private NodeInfo parent;
    private List<NodeInfo> children = new ArrayList<>();

    public NodeInfo() {
    }

    public NodeInfo(AccessibilityNodeInfo node, Resources resources, NodeInfo parent) {
        this.resources = resources;
        this.node = node;
        this.parent = parent;
    }

    public AccessibilityNodeInfo getNode() {
        return node;
    }

    public void setNode(AccessibilityNodeInfo node) {
        this.node = node;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public NodeInfo getParent() {
        return parent;
    }

    public void setParent(NodeInfo parent) {
        this.parent = parent;
    }

    public List<NodeInfo> getChildren() {
        return children;
    }

    public void setChildren(List<NodeInfo> children) {
        this.children = children;
    }
}
