package com.junzixiehui.doraon.core.embedded;

import com.junzixiehui.doraon.anno.CacheConsts;
import com.junzixiehui.doraon.core.CacheConfig;

/**
 * Created on 16/9/7.
 *
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public class EmbeddedCacheConfig<K, V> extends CacheConfig<K, V> {
    private int limit = CacheConsts.DEFAULT_LOCAL_LIMIT;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
