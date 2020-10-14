package com.junzixiehui.doraon.example.cache;

import com.junzixiehui.doraon.core.Cache;
import com.junzixiehui.doraon.core.embedded.CaffeineCacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/10/14  17:26
 * @version: 1.0
 */
public class CacheTest {

	public static void main(String[] args) {



		Cache<String, Integer> cache = CaffeineCacheBuilder.createCaffeineCacheBuilder()
				.limit(100)
				.expireAfterWrite(200, TimeUnit.SECONDS)
				.buildCache();
		cache.put("20161111", 1000000, 1 ,TimeUnit.HOURS);
		Integer orderCount1 = cache.get("20161111");
		Integer orderCount2 = cache.computeIfAbsent("20161212", CacheTest::loadFromDatabase);
		System.out.println(orderCount1);
		System.out.println(orderCount2);
		cache.remove("20161212");
	}

	private static Integer loadFromDatabase(String key) {
		//...
		return 1000;
	}

}
