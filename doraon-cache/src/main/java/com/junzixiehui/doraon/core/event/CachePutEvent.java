package com.junzixiehui.doraon.core.event;

import com.junzixiehui.doraon.core.Cache;
import com.junzixiehui.doraon.core.CacheResult;

/**
 * Created on 2017/2/22.
 *
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public class CachePutEvent extends CacheEvent {
    private long millis;
    private Object key;
    private Object value;
    private CacheResult result;

    public CachePutEvent(Cache cache, long millis, Object key, Object value, CacheResult result) {
        super(cache);
        this.millis = millis;
        this.key = key;
        this.value = value;
        this.result = result;
    }

    public long getMillis() {
        return millis;
    }

    public Object getKey() {
        return key;
    }

    public CacheResult getResult() {
        return result;
    }

    public Object getValue() {
        return value;
    }

}
