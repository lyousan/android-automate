package cn.chci.hmcs.automate.utils;

import java.util.HashMap;
import java.util.Map;

public class BeanContextHolder {
    private static volatile BeanContextHolder instance;

    private BeanContextHolder() {

    }

    public static BeanContextHolder getInstance() {
        if (instance == null) {
            synchronized (BeanContextHolder.class) {
                if (instance == null) {
                    instance = new BeanContextHolder();
                }
            }
        }
        return instance;
    }

    private Map<String, Object> beanMap = new HashMap<>();


    public Object getBean(String beanName) {
        return beanMap.get(beanName);
    }

    public void setBean(String beanName, Object bean) {
        beanMap.put(beanName, bean);
    }

}
