package cn.chci.hmcs.automator.fn;

import java.util.ArrayList;
import java.util.List;

import cn.chci.hmcs.automator.layout.LayoutCache;
import cn.chci.hmcs.automator.model.Command;
import cn.chci.hmcs.automator.model.Node;
import cn.chci.hmcs.automator.layout.LayoutParser;

public class Selector extends Command {

    public String findOne(By by) {
        return LayoutParser.toXMLString(LayoutCache.findOne(by.xpath));
    }

    public List<String> find(By by) {
        List<Node> nodes = LayoutCache.find(by.xpath);
        List<String> result = new ArrayList<>(nodes.size());
        nodes.forEach(nodeInfo -> result.add(LayoutParser.toXMLString(nodeInfo)));
        return result;
    }


}
