package com.junzixiehui.doraon.business.event.guava;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.junzixiehui.doraon.business.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Description: </p>
 * @author: by jxll
 * @date: 2019/5/16  15:08
 * @version: 1.0
 */
@Component
public class EventBusFacade {

	@Autowired
	private EventBus eventBus;
	@Autowired
	private AsyncEventBus asyncEventBus;

	public void post(Event event) {
		if (event == null) {
			return;
		}
		eventBus.post(event);
	}

	public void asyncPost(Event event) {
		if (event == null) {
			return;
		}
		asyncEventBus.post(event);
	}

	public void register(Object bean) {
		if (bean == null) {
			return;
		}
		eventBus.register(bean);
		asyncEventBus.register(bean);
	}

	public void unregister(Object bean) {
		if (bean == null) {
			return;
		}
		eventBus.unregister(bean);
		asyncEventBus.unregister(bean);
	}
}
