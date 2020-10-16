/**
 * Created on 2018/8/11.
 */
package com.junzixiehui.doraon.example.cache.springboot;

import com.junzixiehui.doraon.anno.Cached;

/**
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public interface UserService {
    @Cached(name = "loadUser", expire = 10)
    User loadUser(long userId);
}
