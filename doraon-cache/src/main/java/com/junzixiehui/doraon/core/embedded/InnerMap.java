/**
 * Created on  13-10-17 22:34
 */
package com.junzixiehui.doraon.core.embedded;

import java.util.Collection;
import java.util.Map;

/**
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public interface InnerMap {
    Object getValue(Object key);

    Map getAllValues(Collection keys);

    void putValue(Object key, Object value);

    void putAllValues(Map map);

    boolean removeValue(Object key);

    boolean putIfAbsentValue(Object key, Object value);

    void removeAllValues(Collection keys);
}
