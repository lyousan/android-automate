package cn.chci.hmcs.automator.fn;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.chci.hmcs.automator.layout.LayoutCache;
import cn.chci.hmcs.automator.layout.LayoutInspector;
import cn.chci.hmcs.automator.model.Command;
import cn.chci.hmcs.automator.model.Node;
import cn.chci.hmcs.automator.layout.LayoutParser;

public class Selector extends Command {

    public String findOne(By by) {
        return findOne(by, true);
    }

    public String findOne(By by, Boolean inScreen) {
        List<Node> nodes = LayoutCache.find(by.xpath);
        Node result = null;
        if (inScreen) {
            result = nodes.stream().filter(this::isInScreen).findFirst().get();
        } else {
            result = LayoutCache.findOne(by.xpath);
        }
        return LayoutParser.toXMLString(result);
    }

    public List<String> find(By by) {
        return find(by, true);
    }

    public List<String> find(By by, Boolean inScreen) {
        List<Node> nodes = LayoutCache.find(by.xpath);
        if (inScreen) {
            return nodes.stream().filter(this::isInScreen).map(LayoutParser::toXMLString).collect(Collectors.toList());
        }
        return nodes.stream().map(LayoutParser::toXMLString).collect(Collectors.toList());
    }


    private boolean isInScreen(Node node) {
        if (node == null || node.getNode() == null) {
            return false;
        }
        Rect rect = new Rect();
        node.getNode().getBoundsInScreen(rect);
        return rect.top >= 0 && rect.bottom >= 0 && rect.left >= 0 && rect.right >= 0
                && rect.bottom <= LayoutInspector.boundsInScreen.bottom && rect.right <= LayoutInspector.boundsInScreen.right;
    }

}
