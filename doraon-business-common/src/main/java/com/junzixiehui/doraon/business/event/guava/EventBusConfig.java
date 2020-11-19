package com.junzixiehui.doraon.business.event.guava;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * <p>Description: </p>
 * @author: by jxll
 * @date: 2019/5/16  15:17
 * @version: 1.0
 */
@Configuration
public class EventBusConfig {

	@Bean
	public EventBus eventBus() {
		return new EventBus();
	}

	@Bean
	public AsyncEventBus asyncEventBus() {
		return new AsyncEventBus("asyncEventBus", Executors.newFixedThreadPool(4));
	}
}
