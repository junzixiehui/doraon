/**
 * Created on 2018/8/11.
 */
package com.junzixiehui.doraon.example.cache.springboot;


import com.junzixiehui.doraon.anno.CreateCache;
import com.junzixiehui.doraon.core.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
@Component
public class MyServiceImpl implements MyService {
    @CreateCache(name = "myServiceCache", expire = 60)
    private Cache<String, String> cache;

    @Autowired
    private UserService userService;

    @Override
    public void createCacheDemo() {
        cache.put("myKey", "myValue");
        String myValue = cache.get("myKey");
        System.out.println("get 'myKey' from cache:" + myValue);
    }

    @Override
    public void cachedDemo() {
        userService.loadUser(1);
        userService.loadUser(1);
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		userService.loadUser(1);
    }
}
