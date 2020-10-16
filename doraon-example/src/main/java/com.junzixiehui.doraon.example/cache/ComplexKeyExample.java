package com.junzixiehui.doraon.example.cache;



import com.junzixiehui.doraon.core.Cache;
import com.junzixiehui.doraon.core.embedded.CaffeineCacheBuilder;
import com.junzixiehui.doraon.core.support.FastjsonKeyConvertor;

import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/11/2.
 *
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public class ComplexKeyExample {
    public static void main(String[] args) {
        Cache<Object, Object> cache = CaffeineCacheBuilder.createCaffeineCacheBuilder()
                .limit(100)
                .expireAfterWrite(200, TimeUnit.SECONDS)
                .keyConvertor(FastjsonKeyConvertor.INSTANCE)
                .buildCache();

        DynamicQuery key = new DynamicQuery();
        key.setName("AAA");
        key.setEmail("BBB");
		final Object val1 = cache.get(key);
		final Object val2 = cache.get(key);
		System.out.println(val1);
		System.out.println(val2);
	}


    // no "equals" method
    static class DynamicQuery {
        private long id;
        private String name;
        private String email;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
