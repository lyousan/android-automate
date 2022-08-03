package cn.chci.hmcs.common.toolkit.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author 杨京三
 * @Date 2021/11/3 16:36
 * @Description 该类用于从spring容器中获取bean
 */
@Component
public class SpringUtils implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (this.applicationContext == null) {
            this.applicationContext = applicationContext;
        }
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
