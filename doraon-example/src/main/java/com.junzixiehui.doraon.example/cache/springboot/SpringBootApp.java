/**
 * Created on 2018/8/11.
 */
package com.junzixiehui.doraon.example.cache.springboot;


import com.junzixiehui.doraon.aop.config.EnableCreateCacheAnnotation;
import com.junzixiehui.doraon.aop.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
@SpringBootApplication
@EnableMethodCache(basePackages = "com.junzixiehui.doraon.example")
@EnableCreateCacheAnnotation
public class SpringBootApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootApp.class);
        MyService myService = context.getBean(MyService.class);
        myService.createCacheDemo();
        myService.cachedDemo();
    }
}
