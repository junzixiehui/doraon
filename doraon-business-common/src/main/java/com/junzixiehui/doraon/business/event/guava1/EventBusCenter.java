package com.junzixiehui.doraon.business.event.guava1;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.junzixiehui.doraon.business.util.SpringContextUtilV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * <p>Description: </p>
 *
 * @author: by jxll
 * @date: 2020/3/12  2:40 PM
 * @version: 1.0
 */
@Component
@Slf4j
public class EventBusCenter {

    private EventBus eventBus = new EventBus();
    private AsyncEventBus asyncEventBus = new AsyncEventBus(Executors.newCachedThreadPool());

    public void post(Object event) {
        eventBus.post(event);
		log.info("发送事件:" + event);
    }

    public void postAsync(Object event) {
        asyncEventBus.post(event);
        log.info("发送事件异步:" + event);
    }

    @PostConstruct
    public void init() {
        // 获取所有带有 @EventBusHandler 的 bean，将他们注册为监听者
        List<Object> handlerList = SpringContextUtilV2.getBeansWithAnnotation(EventBusHandler.class);
        for (Object handler : handlerList) {
            asyncEventBus.register(handler);
            eventBus.register(handler);
        }
    }
}
