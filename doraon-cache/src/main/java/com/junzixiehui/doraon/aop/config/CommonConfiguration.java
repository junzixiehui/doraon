/**
 * Created on 2019/6/23.
 */
package com.junzixiehui.doraon.aop.config;

import com.junzixiehui.doraon.support.ConfigMap;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
@Configuration
public class CommonConfiguration {
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ConfigMap jetcacheConfigMap() {
        return new ConfigMap();
    }
}
