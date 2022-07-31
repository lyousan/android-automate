package cn.chci.hmcs.automator.layout;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import cn.chci.hmcs.automator.model.Node;
import cn.chci.hmcs.automator.utils.LayoutInspectorGetter;
import cn.chci.hmcs.automator.utils.StringUtils;

// TODO 界面元素的缓存
public class LayoutCache {
    public static Document document;
    public static Node root;
    private static final SAXReader reader = new SAXReader();

    public static void save(String xml, Node root) throws DocumentException {
        document = reader.read(new ByteArrayInputStream(xml.getBytes()));
        LayoutCache.root = root;
    }

    private static void init() {
        if (document == null || root == null) {
            root = LayoutInspectorGetter.getInstance().captureCurrentWindow();
            try {
                document = reader.read(new ByteArrayInputStream(LayoutParser.toXMLString(root).getBytes()));
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized static void refresh() {
        root = LayoutInspectorGetter.getInstance().captureCurrentWindow();
        try {
            document = reader.read(new ByteArrayInputStream(LayoutParser.toXMLString(root).getBytes()));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static Node findOneByCacheId(String cacheId) {
        List<Node> nodes = searchInTree(root, node -> node.getCacheId().equals(cacheId), true);
        return nodes.size() > 0 ? nodes.get(0) : null;
    }

    public static String findCacheId(String xpath) {
        refresh();
        org.dom4j.Node node = document.selectSingleNode(xpath);
        if (node == null) {
            refresh();
            node = document.selectSingleNode(xpath);
            if (node == null) {
                return "";
            }
        }
        return ((Element) node).attributeValue("cacheId");
    }

    public static List<String> findCacheIds(String xpath) {
        refresh();
        List<org.dom4j.Node> nodes = document.selectNodes(xpath);
        if (nodes.size() == 0) {
            refresh();
            nodes = document.selectNodes(xpath);
        }
        List<String> ids = new ArrayList<>();
        nodes.forEach(node -> ids.add(((Element) node).attributeValue("cacheId")));
        return ids;
    }

    public static List<Node> find(String xpath) {
        List<String> cacheIds = findCacheIds(xpath);
        if (cacheIds.size() == 0) {
            return Collections.emptyList();
        }
        return searchInTree(root, node -> cacheIds.contains(node.getCacheId()), false);
    }

    public static Node findOne(String xpath) {
        String cacheId = findCacheId(xpath);
        if (StringUtils.isEmpty(cacheId)) {
            refresh();
            cacheId = findCacheId(xpath);
        }
        String finalCacheId = cacheId;
        List<Node> nodes = searchInTree(root, node -> node.getCacheId().equals(finalCacheId), true);
        return nodes.size() > 0 ? nodes.get(0) : null;
    }

    private static List<Node> searchInTree(Node root, Predicate<Node> filter, boolean lazy) {
        List<Node> nodes = new ArrayList<>();
        searchInTreeByDeep(root, filter, nodes, lazy);
        return nodes;
    }

    private static void searchInTreeByDeep(Node root, Predicate<Node> filter, List<Node> data, boolean lazy) {
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
        List<Node> children = root.getChildren();
        if (children.size() > 0) {
            for (Node child : children) {
                searchInTreeByDeep(child, filter, data, lazy);
            }
        }
    }

}
