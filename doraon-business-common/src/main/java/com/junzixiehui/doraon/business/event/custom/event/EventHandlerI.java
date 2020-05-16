package com.junzixiehui.doraon.business.event.custom.event;


import com.junzixiehui.doraon.business.event.Event;
import com.junzixiehui.doraon.util.api.Resp;

/**
 * event handler
 *
 */
public interface EventHandlerI<R extends Resp, E extends Event> {

    public R execute(E e);
}
