package com.junzixiehui.doraon.autoconfigure;

import com.junzixiehui.doraon.anno.CacheConsts;
import com.junzixiehui.doraon.core.CacheBuilder;
import com.junzixiehui.doraon.core.embedded.EmbeddedCacheBuilder;

/**
 * Created on 2016/12/2.
 *
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public abstract class EmbeddedCacheAutoInit extends AbstractCacheAutoInit {

    public EmbeddedCacheAutoInit(String... cacheTypes) {
        super(cacheTypes);
    }

    @Override
    protected void parseGeneralConfig(CacheBuilder builder, ConfigTree ct) {
        super.parseGeneralConfig(builder, ct);
        EmbeddedCacheBuilder ecb = (EmbeddedCacheBuilder) builder;

        ecb.limit(Integer.parseInt(ct.getProperty("limit", String.valueOf(CacheConsts.DEFAULT_LOCAL_LIMIT))));
    }
}
