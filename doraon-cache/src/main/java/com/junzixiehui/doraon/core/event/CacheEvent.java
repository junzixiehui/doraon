package com.junzixiehui.doraon.core.event;

import com.junzixiehui.doraon.core.Cache;

/**
 * Created on 2017/2/22.
 *
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public class CacheEvent {

    protected Cache cache;

    public CacheEvent(Cache cache) {
        this.cache = cache;
    }

    public Cache getCache() {
        return cache;
    }

}
