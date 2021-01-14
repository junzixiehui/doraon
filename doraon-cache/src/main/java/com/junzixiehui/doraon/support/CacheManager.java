/**
 * Created on 2019/2/1.
 */
package com.junzixiehui.doraon.support;

import com.junzixiehui.doraon.anno.CacheConsts;
import com.junzixiehui.doraon.core.Cache;
/**
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public interface CacheManager {
    Cache getCache(String area, String cacheName);

    default Cache getCache(String cacheName) {
        return getCache(CacheConsts.DEFAULT_AREA, cacheName);
    }

    static CacheManager defaultManager() {
        return SimpleCacheManager.defaultManager;
    }
}
