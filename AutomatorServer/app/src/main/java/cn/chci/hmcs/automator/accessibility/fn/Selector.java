package cn.chci.hmcs.automator.accessibility.fn;

import android.graphics.Rect;

import cn.chci.hmcs.automator.accessibility.device.DisplayDevice;
import cn.chci.hmcs.automator.accessibility.layout.LayoutCache;
import cn.chci.hmcs.automator.accessibility.layout.LayoutParser;
import cn.chci.hmcs.automator.model.Command;
import cn.chci.hmcs.automator.model.Node;
import cn.chci.hmcs.automator.utils.BeanContextHolder;

import java.util.List;
import java.util.stream.Collectors;

public class Selector extends Command {
    private Rect displayDeviceSize;

    public String findOne(By by) {
        return findOne(by, true);
    }

    public String findOne(By by, Boolean inScreen) {
        List<Node> nodes = LayoutCache.find(by.xpath);
        Node result = null;
        if (inScreen) {
            result = nodes.stream().filter(this::isInScreen).findFirst().orElse(null);
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
        if (displayDeviceSize == null) {
            displayDeviceSize = ((DisplayDevice) BeanContextHolder.getInstance().getBean("displayDevice")).getDeviceSize();
        }
        if (node == null || node.getNode() == null) {
            return false;
        }
        Rect rect = new Rect();
        node.getNode().getBoundsInScreen(rect);
        if (displayDeviceSize != null) {
            return rect.top >= 0 && rect.bottom >= 0 && rect.left >= 0 && rect.right >= 0
                    && rect.bottom <= displayDeviceSize.bottom && rect.right <= displayDeviceSize.right;
        }
        return true;
    }

}
