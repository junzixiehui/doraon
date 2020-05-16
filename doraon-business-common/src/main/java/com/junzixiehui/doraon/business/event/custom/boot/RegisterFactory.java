package com.junzixiehui.doraon.business.event.custom.boot;

import com.junzixiehui.doraon.business.event.custom.event.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RegisterFactory
 *
 * @author fulan.zjf 2017-11-04
 */
@Component
public class RegisterFactory {

	@Autowired
	private EventRegister eventRegister;

	public RegisterI getRegister(Class<?> targetClz) {
		EventHandler eventHandlerAnn = targetClz.getDeclaredAnnotation(EventHandler.class);
		if (eventHandlerAnn != null) {
			return eventRegister;
		}
		return null;
	}
}
