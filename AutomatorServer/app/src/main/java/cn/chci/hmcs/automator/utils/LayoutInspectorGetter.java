package cn.chci.hmcs.automator.utils;

import cn.chci.hmcs.automator.exception.LayoutInspectorNotInitException;
import cn.chci.hmcs.automator.accessibiliy.layout.LayoutInspector;

public class LayoutInspectorGetter {
    private static final BeanContextHolder beanContextHolder = BeanContextHolder.getInstance();

    public static LayoutInspector getInstance() {
        Object obj = beanContextHolder.getBean("layoutInspector");
        if (obj == null) {
            throw new LayoutInspectorNotInitException("布局审查器暂未初始化，请检查是否正常获取到无障碍服务");
        }
        return ((LayoutInspector) obj);
    }
}
