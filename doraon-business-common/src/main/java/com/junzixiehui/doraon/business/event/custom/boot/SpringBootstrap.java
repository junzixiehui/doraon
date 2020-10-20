package com.junzixiehui.doraon.business.event.custom.boot;

import com.junzixiehui.doraon.business.event.custom.event.EventHandler;
import com.junzixiehui.doraon.business.event.custom.event.EventHandlerI;
import com.junzixiehui.doraon.business.util.SpringContextUtil;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.Map;

/**
 * SpringBootstrap
 */
public class SpringBootstrap {

	@Resource
	private EventRegister eventRegister;

	public void init() {
		ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();

		Map<String, Object> eventHandlerBeans = applicationContext.getBeansWithAnnotation(EventHandler.class);
		eventHandlerBeans.values().forEach(eventHandler -> eventRegister.doRegistration((EventHandlerI) eventHandler));
	}
}
