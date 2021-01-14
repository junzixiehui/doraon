package com.junzixiehui.doraon.business.event.custom.event;

import com.junzixiehui.doraon.business.event.Event;
import com.junzixiehui.doraon.util.exception.ServiceException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件控制中枢
 */
@SuppressWarnings("rawtypes")
@Component
public class EventHub {

	private HashMap<Class, List<EventHandlerI>> eventRepository = new HashMap<>();
	private Map<Class, Class> responseRepository = new HashMap<>();

	public HashMap<Class, List<EventHandlerI>> getEventRepository() {
		return eventRepository;
	}

	public void setEventRepository(HashMap<Class, List<EventHandlerI>> eventRepository) {
		this.eventRepository = eventRepository;
	}

	public Map<Class, Class> getResponseRepository() {
		return responseRepository;
	}

	public List<EventHandlerI> getEventHandler(Class eventClass) {
		List<EventHandlerI> eventHandlerIList = findHandler(eventClass);
		if (eventHandlerIList == null || eventHandlerIList.size() == 0) {
			throw new ServiceException(eventClass + "is not registered in eventHub, please register first");
		}
		return eventHandlerIList;
	}

	/**
	 * 注册事件
	 * @param eventClz
	 * @param executor
	 */
	public void register(Class<? extends Event> eventClz, EventHandlerI executor) {
		List<EventHandlerI> eventHandlers = eventRepository.get(eventClz);
		if (eventHandlers == null) {
			eventHandlers = new ArrayList<>();
			eventRepository.put(eventClz, eventHandlers);
		}
		eventHandlers.add(executor);
	}

	private List<EventHandlerI> findHandler(Class<? extends Event> eventClass) {
		List<EventHandlerI> eventHandlerIList = null;
		Class cls = eventClass;
		eventHandlerIList = eventRepository.get(cls);
		return eventHandlerIList;
	}
}
