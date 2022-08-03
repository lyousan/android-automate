package cn.chci.hmcs.automator.fn;

import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;

import cn.chci.hmcs.automator.exception.NodeChangedException;
import cn.chci.hmcs.automator.exception.NodeInoperableException;
import cn.chci.hmcs.automator.layout.LayoutCache;
import cn.chci.hmcs.automator.model.Command;
import cn.chci.hmcs.automator.model.Node;

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
        if (!node.getNode().isClickable()) {
            throw new NodeInoperableException("该节点不可执行此操作 ==> click");
        }
        return node.getNode().performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    public boolean longClick(String cacheId) {
        Node node = LayoutCache.findOneByCacheId(cacheId);
        if (node == null || !node.getCacheId().equals(cacheId)) {
            throw new NodeChangedException("该节点或许已经发生变化了");
        }
        if (!node.getNode().isLongClickable()) {
            throw new NodeInoperableException("该节点不可执行此操作 ==> longClick");
        }
        return node.getNode().performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
    }

    public boolean input(String cacheId, String text) {
        Node node = LayoutCache.findOneByCacheId(cacheId);
        if (node == null || !node.getCacheId().equals(cacheId)) {
            throw new NodeChangedException("该节点或许已经发生变化了");
        }
        if (!node.getNode().isEditable()) {
            throw new NodeInoperableException("该节点不可执行此操作 ==> edit");
        }
        Bundle bundle = new Bundle();
        bundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
        return node.getNode().performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle);
    }

}
