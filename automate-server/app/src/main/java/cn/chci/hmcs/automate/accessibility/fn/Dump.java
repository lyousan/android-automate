package cn.chci.hmcs.automate.accessibility.fn;

import cn.chci.hmcs.automate.accessibility.layout.LayoutInspector;
import cn.chci.hmcs.automate.model.Command;
import cn.chci.hmcs.automate.model.Node;
import cn.chci.hmcs.automate.accessibility.layout.LayoutParser;
import cn.chci.hmcs.automate.utils.LayoutInspectorGetter;

public class Dump extends Command {

    public String dump() {
        LayoutInspector layoutInspector = LayoutInspectorGetter.getInstance();
        Node node = layoutInspector.captureCurrentWindow();
        return LayoutParser.toXMLString(node);
    }
}
