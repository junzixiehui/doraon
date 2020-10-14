package com.junzixiehui.doraon.core.external;

import com.junzixiehui.doraon.core.CacheConfig;
import com.junzixiehui.doraon.core.support.DecoderMap;
import com.junzixiehui.doraon.core.support.JavaValueEncoder;

import java.util.function.Function;

/**
 * Created on 16/9/9.
 *
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public class ExternalCacheConfig<K, V> extends CacheConfig<K, V> {
    private String keyPrefix;
    private Function<Object, byte[]> valueEncoder = JavaValueEncoder.INSTANCE;
    private Function<byte[], Object> valueDecoder = DecoderMap.defaultJavaValueDecoder();

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public Function<Object, byte[]> getValueEncoder() {
        return valueEncoder;
    }

    public void setValueEncoder(Function<Object, byte[]> valueEncoder) {
        this.valueEncoder = valueEncoder;
    }

    public Function<byte[], Object> getValueDecoder() {
        return valueDecoder;
    }

    public void setValueDecoder(Function<byte[], Object> valueDecoder) {
        this.valueDecoder = valueDecoder;
    }
}
