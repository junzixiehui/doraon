package com.junzixiehui.doraon.method;


import com.junzixiehui.doraon.support.CacheContext;
import com.junzixiehui.doraon.support.ConfigMap;
import com.junzixiehui.doraon.support.GlobalCacheConfig;
import org.springframework.context.ApplicationContext;

/**
 * Created on 2016/10/19.
 *
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public class SpringCacheContext extends CacheContext {

    private ApplicationContext applicationContext;

    public SpringCacheContext(GlobalCacheConfig globalCacheConfig, ApplicationContext applicationContext) {
        super(globalCacheConfig);
        this.applicationContext = applicationContext;
    }

    @Override
    protected CacheInvokeContext newCacheInvokeContext() {
        return new SpringCacheInvokeContext(applicationContext);
    }

    @Override
    public synchronized void init() {
        if (applicationContext != null) {
            ConfigMap configMap = applicationContext.getBean(ConfigMap.class);
            cacheManager.setCacheCreator((area, cacheName) -> {
                CacheInvokeConfig cic = configMap.getByCacheName(area, cacheName);
                if (cic == null) {
                    throw new IllegalArgumentException("cache definition not found: area=" + area + ",cacheName=" + cacheName);
                }
                return __createOrGetCache(cic.getCachedAnnoConfig(), area, cacheName);
            });
        }
        super.init();
    }
}
