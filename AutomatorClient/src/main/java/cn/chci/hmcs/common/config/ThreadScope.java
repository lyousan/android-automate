package cn.chci.hmcs.common.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 杨京三
 * @Date 2022/4/25 16:47
 * @Description 线程域，被@Scope("thread")标记的bean，作用域将会被限定在同一个线程中，
 * 即，在同一个线程中的任何位置都将获得同一个bean，非常契合一次任务配备一条线程来执行的运行方式，减少了线程不安全的情况，
 * 不过还是非常需要注意类初始化的时机，防止在bean依赖于其他bean或其他bean中的成员变量时，被依赖的bean或相关成员变量还未初始化，从而导致一些难以预料的问题
 **/
@Component
public class ThreadScope implements Scope {
    private static final ThreadLocal threadLocal = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return new HashMap();
        }
    };

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Map scope = (Map) threadLocal.get();
        Object object = scope.get(name);
        if (object == null) {
            object = objectFactory.getObject();
            scope.put(name, object);
        }
        return object;
    }

    @Override
    public Object remove(String name) {
        Map scope = (Map) threadLocal.get();
        return scope.remove(name);
    }

    @Override
    public void registerDestructionCallback(String s, Runnable runnable) {

    }

    @Override
    public Object resolveContextualObject(String s) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }

    /**
     * 清空当前线程的所有bean
     */
    public void clear() {
        Map scope = (Map) threadLocal.get();
        scope.clear();
        threadLocal.remove();
    }
}
