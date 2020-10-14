package com.junzixiehui.doraon.core.external;

import com.junzixiehui.doraon.anno.CacheConsts;

public class MockRemoteCacheConfig<K, V> extends ExternalCacheConfig<K, V> {
    private int limit = CacheConsts.DEFAULT_LOCAL_LIMIT;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
