package com.junzixiehui.doraon.core;

import com.junzixiehui.doraon.core.event.CacheEvent;

/**
 * Created on 2016/10/25.
 *
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
@FunctionalInterface
public interface CacheMonitor {

    void afterOperation(CacheEvent event);

}
