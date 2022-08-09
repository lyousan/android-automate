package cn.chci.hmcs.automate.accessibility.fn;

import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.function.Predicate;

import cn.chci.hmcs.automate.exception.NodeChangedException;
import cn.chci.hmcs.automate.exception.NodeInoperableException;
import cn.chci.hmcs.automate.accessibility.layout.LayoutCache;
import cn.chci.hmcs.automate.model.Command;
import cn.chci.hmcs.automate.model.Node;

public class NodeActions extends Command {
    /**
     * 点击指定元素
     *
     * @param cacheId 该节点的cacheId，用于从缓存的虚拟节点中查找真实节点
     */
    public boolean click(String cacheId) {
        Node node = LayoutCache.findOneByCacheId(cacheId);
        if (node == null || !node.getCacheId().equals(cacheId)) {
            throw new NodeChangedException("该节点或许已经发生变化了");
        }
        node = bubbleFind(node, n -> n.getNode().isClickable());
        if (node == null) {
            throw new NodeInoperableException("找不到可以执行此操作的节点 ==> click");
        }
        return node.getNode().performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    public boolean longClick(String cacheId) {
        Node node = LayoutCache.findOneByCacheId(cacheId);
        if (node == null || !node.getCacheId().equals(cacheId)) {
            throw new NodeChangedException("该节点或许已经发生变化了");
        }
        node = bubbleFind(node, n -> n.getNode().isLongClickable());
        if (node == null) {
            throw new NodeInoperableException("找不到可以执行此操作的节点 ==> longClick");
        }
        return node.getNode().performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
    }

    public boolean input(String cacheId, String text) {
        Node node = LayoutCache.findOneByCacheId(cacheId);
        if (node == null || !node.getCacheId().equals(cacheId)) {
            throw new NodeChangedException("该节点或许已经发生变化了");
        }
        node = bubbleFind(node, n -> n.getNode().isEditable());
        if (node == null) {
            throw new NodeInoperableException("找不到可以执行此操作的节点 ==> edit");
        }
        Bundle bundle = new Bundle();
        bundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
        return node.getNode().performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle);
    }

    public boolean scrollUp(String cacheId) {
        Node node = LayoutCache.findOneByCacheId(cacheId);
        if (node == null || !node.getCacheId().equals(cacheId)) {
            throw new NodeChangedException("该节点或许已经发生变化了");
        }
        node = bubbleFind(node, n -> n.getNode().isScrollable());
        if (node == null) {
            throw new NodeInoperableException("找不到可以执行此操作的节点 ==> scrollable");
        }
        return node.getNode().performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
    }

    public boolean scrollDown(String cacheId) {
        Node node = LayoutCache.findOneByCacheId(cacheId);
        if (node == null || !node.getCacheId().equals(cacheId)) {
            throw new NodeChangedException("该节点或许已经发生变化了");
        }
        node = bubbleFind(node, n -> n.getNode().isScrollable());
        if (node == null) {
            throw new NodeInoperableException("找不到可以执行此操作的节点 ==> scrollable");
        }
        return node.getNode().performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
    }

    private Node bubbleFind(Node node, Predicate<Node> predicate) {
        if (node == null) {
            return null;
        }
        if (predicate.test(node)) {
            return node;
        }
        return bubbleFind(node.getParent(), predicate);
    }

}
