package cn.chci.hmcs.automate.accessibility.fn;

import cn.chci.hmcs.automate.model.Node;

import java.util.List;

/**
 * @author 有三
 * @date 2023-03-23 10:59
 * @description
 **/
public interface Selectable {

    List<Node> find(By by);
    List<Node> find(By by,boolean inScreen);

    Node findOne(By by);

    Node findOne(By by,boolean inScreen);
}
