package com.junzixiehui.doraon.core.external;

import com.junzixiehui.doraon.core.AbstractCache;
import com.junzixiehui.doraon.core.CacheConfigException;
import com.junzixiehui.doraon.core.CacheException;

import java.io.IOException;

/**
 * Created on 2016/10/8.
 *
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public abstract class AbstractExternalCache<K, V> extends AbstractCache<K, V> {

    private ExternalCacheConfig<K, V> config;

    public AbstractExternalCache(ExternalCacheConfig<K, V> config) {
        this.config = config;
        checkConfig();
    }

    protected void checkConfig() {
        if (config.getValueEncoder() == null) {
            throw new CacheConfigException("no value encoder");
        }
        if (config.getValueDecoder() == null) {
            throw new CacheConfigException("no value decoder");
        }
        if (config.getKeyPrefix() == null){
            throw new CacheConfigException("keyPrefix is required");
        }
    }

    public byte[] buildKey(K key) {
        try {
            Object newKey = key;
            if (key instanceof byte[]) {
                newKey = key;
            } else if(key instanceof String){
                newKey = key;
            } else {
                if (config.getKeyConvertor() != null) {
                    newKey = config.getKeyConvertor().apply(key);
                }
            }
            return ExternalKeyUtil.buildKeyAfterConvert(newKey, config.getKeyPrefix());
        } catch (IOException e) {
            throw new CacheException(e);
        }
    }

}
