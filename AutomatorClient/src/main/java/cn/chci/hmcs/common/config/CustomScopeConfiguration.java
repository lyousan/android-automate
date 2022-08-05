package cn.chci.hmcs.common.config;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 杨京三
 * @Date 2022/4/25 16:51
 * @Description
 **/
@Configuration
public class CustomScopeConfiguration {
    @Bean
    public static CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
        Map<String, Object> map = new HashMap<String, Object>(2);
        // 配置scope，这里的key就是用于@Scope()注解中的值
        map.put("thread", new ThreadScope());
        customScopeConfigurer.setScopes(map);
        return customScopeConfigurer;
    }
}
