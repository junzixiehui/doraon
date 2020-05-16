package com.junzixiehui.doraon.business.event.custom.event;


import com.junzixiehui.doraon.business.event.Event;
import com.junzixiehui.doraon.business.event.custom.exception.InfraException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件控制中枢
 */
@SuppressWarnings("rawtypes")
@Component
public class EventHub {
    @Getter
    @Setter
    private Map<Class, EventHandlerI> eventRepository = new HashMap<>();
    
    @Getter
    private Map<Class, Class> responseRepository = new HashMap<>();
    
    public EventHandlerI getEventHandler(Class eventClass) {
        EventHandlerI eventHandlerI = findHandler(eventClass);
        if (eventHandlerI == null) {
            throw new InfraException(eventClass + "is not registered in eventHub, please register first");
        }
        return eventHandlerI;
    }

    /**
     * 注册事件
     * @param eventClz
     * @param executor
     */
    public void register(Class<? extends Event> eventClz, EventHandlerI executor){
        eventRepository.put(eventClz, executor);
    }

    private EventHandlerI findHandler(Class<? extends Event> eventClass){
        EventHandlerI eventHandlerI = null;
        Class cls = eventClass;
        eventHandlerI = eventRepository.get(cls);
        return eventHandlerI;
    }

}
