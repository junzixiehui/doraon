package com.junzixiehui.doraon.example;

import com.junzixiehui.doraon.aop.config.EnableCreateCacheAnnotation;
import com.junzixiehui.doraon.aop.config.EnableMethodCache;
import com.junzixiehui.doraon.business.util.EnvHelper;
import com.junzixiehui.doraon.example.cache.springboot.MyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.StringUtils;

/**
 * 应用启动类
 * @date 2018/3/5
 */
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableMethodCache(basePackages = "com.junzixiehui.doraon.example")
@EnableCreateCacheAnnotation
public class Main {
    public static final String APPLICATION_NAME = System.getProperty("spring.application.name");

    public static void main(String[] args) throws Exception {
        if (StringUtils.isEmpty(APPLICATION_NAME)) {
            throw new Exception("Start application, must set environment variable:spring.application.name");
        }
        ConfigurableApplicationContext context = new SpringApplication(Main.class).run(args);

        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        String active = StringUtils.arrayToCommaDelimitedString(activeProfiles);
        EnvHelper.setActive(active);
        //log.info("========当前环境:" + active + "|http://" + IPUtils.getLocalIpv4() + ":" + "8943");


		MyService myService = context.getBean(MyService.class);
		myService.createCacheDemo();
		myService.cachedDemo();
    }
}
