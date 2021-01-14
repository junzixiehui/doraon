package com.junzixiehui.doraon.business.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>Description: guava 抽象缓冲</p>
 * @author: by jxll
 * @date: 2019/6/4  16:11
 * @version: 1.0
 */
@Slf4j
public abstract class AbstractGuavaCache<K, V> {

	protected int refreshDuration;//缓存自动刷新周期
	protected int expireAfterWrite;//缓存项在给定时间内没有被写访问（创建或覆盖），则回收。
	protected int expireAfterAccess;//缓存项在给定时间内没有被读/写访问，则回收
	protected TimeUnit timeUnit;
	protected int cacheMaximumSize;
	protected boolean recordStats;
	private LoadingCache<K, V> cache;
	private ListeningExecutorService backgroundRefreshPools = MoreExecutors
			.listeningDecorator(Executors.newFixedThreadPool(4));


	protected abstract void initCacheFields();

	public void cleanCache(K key) {
		cache.invalidate(key);
	}
	public void clear() {
		cache.invalidateAll();
	}
	public CacheStats stats() {
		return cache.stats();
	}
	public ConcurrentMap asMap() {
		return cache.asMap();
	}

	/**
	 * @description: 定义缓存值的计算方法
	 * @description: 新值计算失败时抛出异常，get操作时将继续返回旧的缓存
	 */
	protected abstract V getValueWhenExpire(K key);

	private LoadingCache<K, V> getCache() {
		if (cache == null) {
			synchronized (this) {
				if (cache == null) {
					initCacheFields();
					final CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();
					cacheBuilder.maximumSize(cacheMaximumSize);
					if (recordStats){
						cacheBuilder.recordStats();
					}
					cacheBuilder.recordStats();
					if (refreshDuration > 0){
						cacheBuilder.refreshAfterWrite(refreshDuration, timeUnit);
					}
					if (expireAfterAccess > 0){
						cacheBuilder.expireAfterWrite(expireAfterAccess,timeUnit);
					}
					if (expireAfterWrite > 0){
						cacheBuilder.expireAfterWrite(expireAfterWrite,timeUnit);
					}
					cache = cacheBuilder.build(new CacheLoader<K, V>() {
								@Override
								public V load(K key) throws Exception {
									return getValueWhenExpire(key);
								}

								@Override
								public ListenableFuture<V> reload(final K key, final V oldValue) throws Exception {
									return backgroundRefreshPools.submit(new Callable<V>() {
										@Override
										public V call() throws Exception {
											try {
												return getValueWhenExpire(key);
											} catch (Exception e) {
												log.error("getCache error", e);
												return oldValue;
											} finally {
												log.info("reload {} done.", key);
											}
										}
									});
								}
							});
				}
			}
		}
		return cache;
	}

	/**
	 * @description: 从cache中拿出数据的操作
	 */
	public V getValueFromCache(K key) throws Exception {
		return getCache().get(key);
	}
}
