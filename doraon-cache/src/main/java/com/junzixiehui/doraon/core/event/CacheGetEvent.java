package com.junzixiehui.doraon.core.event;

import com.junzixiehui.doraon.core.Cache;
import com.junzixiehui.doraon.core.CacheGetResult;

/**
 * Created on 2017/2/22.
 *
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public class CacheGetEvent extends CacheEvent {

    private long millis;
    private Object key;
    private CacheGetResult result;

    public CacheGetEvent(Cache cache, long millis, Object key, CacheGetResult result) {
        super(cache);
        this.millis = millis;
        this.key = key;
        this.result = result;
    }

    public long getMillis() {
        return millis;
    }

    public Object getKey() {
        return key;
    }

    public CacheGetResult getResult() {
        return result;
    }

}
