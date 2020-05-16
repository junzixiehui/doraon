package com.junzixiehui.doraon.business.event.custom.event;

import com.junzixiehui.doraon.business.event.Event;
import com.junzixiehui.doraon.util.api.Resp;

/**
 * EventBus interface
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
public interface EventBusI {

    /**
     * Send event to EventBus
     * 
     * @param event
     * @return Response
     */
    public Resp fire(Event event);
}
