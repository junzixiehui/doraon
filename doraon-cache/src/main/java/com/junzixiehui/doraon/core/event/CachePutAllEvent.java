package com.junzixiehui.doraon.core.event;

import com.junzixiehui.doraon.core.Cache;
import com.junzixiehui.doraon.core.CacheResult;

import java.util.Map;

/**
 * Created on 2017/2/22.
 *
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public class CachePutAllEvent extends CacheEvent {
    private final long millis;
    private final Map map;
    private final CacheResult result;

    public CachePutAllEvent(Cache cache, long millis, Map map, CacheResult result) {
        super(cache);
        this.millis = millis;
        this.map = map;
        this.result = result;
    }

    public long getMillis() {
        return millis;
    }

    public Map getMap() {
        return map;
    }

    public CacheResult getResult() {
        return result;
    }
}
