package cn.chci.hmcs.automator.utils;

import cn.chci.hmcs.automator.model.Node;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @Author 有三
 * @Date 2022-07-28 22:29
 * @Description
 **/
public class NodeParser {
    private static final SAXReader XML_READER = new SAXReader();

    public static Node parse(String xml) {
        if (StringUtils.isEmpty(xml)) {
            return null;
        }
        Node node = null;
        try {
            Document doc = XML_READER.read(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            Element root = doc.getRootElement();
            node = doParse(root);
            parse(root, node, true);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return node;
    }

    private static void parse(Element element, Node parent, boolean isRoot) {
        Node node = doParse(element);
        if (!isRoot) {
            node.setParent(parent);
        }
        Iterator<Element> iterator = element.elementIterator();
        if (iterator.hasNext()) {
            while (iterator.hasNext()) {
                Element child = iterator.next();
                parse(child, node, false);
            }
        } else {
            if (!isRoot) {
                parent.getChildren().add(node);
            }
        }
    }

    private static Node doParse(Element element) {
        Node node = new Node();
        node.setId(element.attributeValue("resource-id"));
        node.setClassName(element.attributeValue("className"));
        node.setText(element.attributeValue("text"));
        node.setContentDesc(element.attributeValue("contentDesc"));
        node.setCheckable("true".equalsIgnoreCase(element.attributeValue("checkable")));
        node.setChecked("true".equalsIgnoreCase(element.attributeValue("checked")));
        node.setClickable("true".equalsIgnoreCase(element.attributeValue("clickable")));
        node.setEnabled("true".equalsIgnoreCase(element.attributeValue("enabled")));
        node.setFocusable("true".equalsIgnoreCase(element.attributeValue("focusable")));
        node.setFocused("true".equalsIgnoreCase(element.attributeValue("focused")));
        node.setScrollable("true".equalsIgnoreCase(element.attributeValue("scrollable")));
        node.setLongClickable("true".equalsIgnoreCase(element.attributeValue("longClickable")));
        node.setPassword("true".equalsIgnoreCase(element.attributeValue("password")));
        node.setSelected("true".equalsIgnoreCase(element.attributeValue("selected")));
        node.setRect(bounds2Rect(element.attributeValue("bounds")));
        return node;
    }

    private static Rectangle bounds2Rect(String bounds) {
        Rectangle rectangle = null;
        if (bounds == null || "".equals(bounds)) {
            return rectangle;
        }
        String[] xy0 = bounds.split("]\\[")[0].replace("[", "").split(",");
        String[] xy1 = bounds.split("]\\[")[1].replace("]", "").split(",");
        int x0 = Integer.parseInt(xy0[0]);
        int y0 = Integer.parseInt(xy0[1]);
        int x1 = Integer.parseInt(xy1[0]);
        int y1 = Integer.parseInt(xy1[1]);
        rectangle = new Rectangle(x0, y0, y1 - y0, x1 - x0);
        return rectangle;
    }

}
