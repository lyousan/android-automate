package cn.chci.hmcs.automator.layout;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.security.acl.NotOwnerException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import cn.chci.hmcs.automator.utils.BeanContextHolder;
import cn.chci.hmcs.automator.utils.LayoutInspectorGetter;

// TODO 界面元素的缓存
public class LayoutCache {
    public static Document document;
    public static NodeInfo root;
    private static final SAXReader reader = new SAXReader();

    public static void save(String xml, NodeInfo root) throws DocumentException {
        document = reader.read(new ByteArrayInputStream(xml.getBytes()));
        LayoutCache.root = root;
    }

    private static void init() {
        if (document == null || root == null) {
            root = LayoutInspectorGetter.getInstance().captureCurrentWindow();
            try {
                document = reader.read(new ByteArrayInputStream(NodeInfoParser.toXMLString(root).getBytes()));
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    public static String findCacheId(String xpath) {
        init();
        Node node = document.selectSingleNode(xpath);
        if (node == null) {
            return "";
        }
        return ((Element) node).attributeValue("cacheId");
    }

    public static List<String> findCacheIds(String xpath) {
        init();
        List<Node> nodes = document.selectNodes(xpath);
        List<String> ids = new ArrayList<>();
        nodes.forEach(node -> ids.add(((Element) node).attributeValue("cacheId")));
        return ids;
    }

    public static List<NodeInfo> find(String xpath) {
        init();
        List<String> cacheIds = findCacheIds(xpath);
        if (cacheIds.size() == 0) {
            return Collections.emptyList();
        }
        return searchInTree(root, node -> cacheIds.contains(node.getCacheId()), false);
    }

    public static NodeInfo findOne(String xpath) {
        init();
        String cacheId = findCacheId(xpath);
        List<NodeInfo> nodes = searchInTree(root, node -> node.getCacheId().equals(cacheId), true);
        return nodes.size() > 0 ? nodes.get(0) : null;
    }

    private static List<NodeInfo> searchInTree(NodeInfo root, Predicate<NodeInfo> filter, boolean lazy) {
        List<NodeInfo> nodes = new ArrayList<>();
        searchInTreeByDeep(root, filter, nodes, lazy);
        return nodes;
    }

    private static void searchInTreeByDeep(NodeInfo root, Predicate<NodeInfo> filter, List<NodeInfo> data, boolean lazy) {
        // 惰性查找，开启lazy模式时将只只查找第一个满足条件的节点
        if (lazy && data.size() > 0) {
            return;
        }
        if (filter.test(root)) {
            data.add(root);
            if (lazy) {
                return;
            }
        }
        List<NodeInfo> children = root.getChildren();
        if (children.size() > 0) {
            for (NodeInfo child : children) {
                searchInTreeByDeep(child, filter, data, lazy);
            }
        }
    }

}
