package cn.chci.hmcs.automator.fn;

import cn.chci.hmcs.automator.accessibiliy.layout.LayoutInspector;
import cn.chci.hmcs.automator.model.Command;
import cn.chci.hmcs.automator.model.Node;
import cn.chci.hmcs.automator.accessibiliy.layout.LayoutParser;
import cn.chci.hmcs.automator.utils.LayoutInspectorGetter;

public class Dump extends Command {

    public String dump() {
        LayoutInspector layoutInspector = LayoutInspectorGetter.getInstance();
        Node node = layoutInspector.captureCurrentWindow();
        return LayoutParser.toXMLString(node);
    }
}
