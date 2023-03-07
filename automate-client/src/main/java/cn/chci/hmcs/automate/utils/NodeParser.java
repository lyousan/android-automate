package cn.chci.hmcs.automate.utils;

import cn.chci.hmcs.automate.model.Node;
import cn.chci.hmcs.automate.socket.Client;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

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
    /**
     * 这个SAXReader在多线程情况下是存在问题的，多个线程同时解析时会抛出一个{@link org.xml.sax.SAXException}异常，
     * FWK005 parse may not be called while parsing.
     * 异常的原因是在转换Doc的时候又来了一个请求转换
     * 直接加锁对性能影响很大，本就耗时的过程还需要排队了，所有这里采用ThreadLocal优化，每个线程独享一个reader,
     * 但是实测下来 直接加锁和ThreadLocal的性能上区别不大(只用了两台手机，量比较小)，而且都能解决这个异常
     */
//    @Deprecated
//    private static final SAXReader XML_READER = new SAXReader();
    public static final ThreadLocal<SAXReader> localReader = new ThreadLocal<>();

    public static SAXReader getReader() {
        SAXReader reader = localReader.get();
        if (reader == null) {
            reader = new SAXReader();
            localReader.set(reader);
        }
        return reader;
    }

    public static Node parse(String xml) {
        return parse(xml, null);
    }

    public static Node parse(String xml, Client client) {
        if (StringUtils.isEmpty(xml)) {
            return null;
        }
        Node node = null;
        try {
            Document doc = getReader().read(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            Element root = doc.getRootElement();
            node = parse(root, null, client, true);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return node;
    }

    private static Node parse(Element element, Node parent, Client client, boolean isRoot) {
        Node node = doParse(element);
        node.setClient(client);
        if (!isRoot) {
            node.setParent(parent);
        }
        Iterator<Element> iterator = element.elementIterator();
        if (iterator.hasNext()) {
            Node tmp;
            while (iterator.hasNext()) {
                Element child = iterator.next();
                tmp = parse(child, node, client, false);
                if (!node.getChildren().contains(tmp)) {
                    node.getChildren().add(tmp);
                }
            }
        } else {
            if (!isRoot) {
                parent.getChildren().add(node);
            }
        }
        return node;
    }

    private static Node doParse(Element element) {
        Node node = new Node();
        node.setId(element.attributeValue("resource-id"));
        node.setClassName(element.attributeValue("class"));
        node.setText(element.attributeValue("text"));
        node.setContentDesc(element.attributeValue("content-desc"));
        node.setCheckable("true".equalsIgnoreCase(element.attributeValue("checkable")));
        node.setChecked("true".equalsIgnoreCase(element.attributeValue("checked")));
        node.setClickable("true".equalsIgnoreCase(element.attributeValue("clickable")));
        node.setEnabled("true".equalsIgnoreCase(element.attributeValue("enabled")));
        node.setEditable("true".equalsIgnoreCase(element.attributeValue("editable")));
        node.setFocusable("true".equalsIgnoreCase(element.attributeValue("focusable")));
        node.setFocused("true".equalsIgnoreCase(element.attributeValue("focused")));
        node.setScrollable("true".equalsIgnoreCase(element.attributeValue("scrollable")));
        node.setLongClickable("true".equalsIgnoreCase(element.attributeValue("long-clickable")));
        node.setPassword("true".equalsIgnoreCase(element.attributeValue("password")));
        node.setSelected("true".equalsIgnoreCase(element.attributeValue("selected")));
        node.setRect(bounds2Rect(element.attributeValue("bounds")));
        node.setCacheId(element.attributeValue("cacheId"));
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
        rectangle = new Rectangle(x0, y0, x1 - x0, y1 - y0);
        return rectangle;
    }

}
