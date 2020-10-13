package com.junzixiehui.doraon.business.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 封装RedisTemplate, 它有些方法定义不合理<br>
 * 注意：
 * 1、所有方法给key默认加上前缀
 * 2、校验key长度不能超过设定值
 * 3、key内容只能包含字母,数字,和特殊字符":"
 * 4、不涉及到写redis的方法不需要checkKey，只用getNewKey就行
 */
@Slf4j
@Component
public class RedisTemplateWarpper {

    @Autowired
    private RedisConfig redisConfig;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Resource(name = "objectRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, String> valueOperations;
    private ListOperations<String, Object> listOperations;
    private HashOperations<String, String, Object> hashOperations;
    private ZSetOperations<String, Object> zSetOperations;

    @PostConstruct
    void afterPropertiesSet() {
        valueOperations = stringRedisTemplate.opsForValue();
        listOperations = redisTemplate.opsForList();
        hashOperations = redisTemplate.opsForHash();
        zSetOperations = redisTemplate.opsForZSet();
    }

    /**
     * 添加默认key前缀生成新key
     *
     * @param oldKey
     * @return
     */
    private String getNewKey(String oldKey) {
        String prefix = redisConfig.getKeyPrefix() + RedisConfig.KEY_DELIMITER;
        if (oldKey.startsWith(prefix)) {
            return oldKey;
        } else {
            return prefix + oldKey;
        }
    }

    /**
     * 数据进入redis之前校验key,检验通过后添加默认key前缀
     *
     * @param key
     */
    private String checkKey(String key) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("redis key must be not empty");
        }

        if (key.length() > redisConfig.getKeyMaxLength()) {
            throw new IllegalArgumentException("redis key[" + key + "] length must be less than " + redisConfig.getKeyMaxLength());
        }
        return this.getNewKey(key);
    }

    /**
     * =====================================Value相关操作====================================
     */
    public String vGet(String key) {
        return valueOperations.get(getNewKey(key));
    }

    public String vGetAndSet(String key, String value) {
        return valueOperations.getAndSet(checkKey(key), value);
    }

    public void vSet(String key, String value) {
        valueOperations.set(checkKey(key), value);
    }

    public void vSet(String key, String value, long timeout, TimeUnit unit) {
        valueOperations.set(checkKey(key), value, timeout, unit);
    }

    public Boolean vSetIfAbsent(String key, String value) {
        return valueOperations.setIfAbsent(checkKey(key), value);
    }

    public Boolean vSetIfAbsent(String key, String value, long timeoutMillisecond) {
        RedisSerializer<String> stringSerializer = stringRedisTemplate.getStringSerializer();
        return stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                Object obj = connection.execute("set",
                        stringSerializer.serialize(checkKey(key)),
                        stringSerializer.serialize(value),
                        stringSerializer.serialize("NX"),
                        stringSerializer.serialize("PX"),
                        stringSerializer.serialize(String.valueOf(timeoutMillisecond)));
                return obj != null;
            }
        });
    }

    public void vMultiSet(Map<String, String> map) {
        valueOperations.multiSet(map);
    }

    public List<String> vMultiGet(Collection<String> keys) {
        return valueOperations.multiGet(keys);
    }

    public Long vIncr(String key) {
        return valueOperations.increment(checkKey(key), 1L);
    }

    public Long vIncrBy(String key, long delta) {
        return valueOperations.increment(checkKey(key), delta);
    }

    public Long vDecr(String key) {
        return valueOperations.increment(checkKey(key), -1L);
    }

    public Long vDecrBy(String key, long delta) {
        return valueOperations.increment(checkKey(key), -delta);
    }

    /**
     * =====================================Keys相关操作====================================
     */
    public void kDelete(String key) {
        redisTemplate.delete(getNewKey(key));
    }

    public void kDelete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    public Boolean kExists(String key) {
        return redisTemplate.hasKey(getNewKey(key));
    }

    public Boolean kExpire(String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(getNewKey(key), timeout, unit);
    }

    public Boolean kExpireAt(String key, final Date date) {
        return redisTemplate.expireAt(getNewKey(key), date);
    }

    public Long kGetExpire(String key) {
        return redisTemplate.getExpire(getNewKey(key));
    }

    public Long kGetExpire(String key, final TimeUnit timeUnit) {
        return redisTemplate.getExpire(getNewKey(key), timeUnit);
    }

    /**
     * =====================================List相关操作====================================
     */
    public List<Object> lRange(String key, long start, long end) {
        return listOperations.range(getNewKey(key), start, end);
    }

    public void lTrim(String key, long start, long end) {
        listOperations.trim(getNewKey(key), start, end);
    }

    public Long lLen(String key) {
        return listOperations.size(getNewKey(key));
    }

    public Long lLeftPush(String key, Object value) {
        return listOperations.leftPush(checkKey(key), value);
    }

    public Long lLeftPushAll(String key, Object... values) {
        return listOperations.leftPushAll(checkKey(key), values);
    }

    public Long lLeftPushAll(String key, Collection<Object> values) {
        return listOperations.leftPushAll(checkKey(key), values);
    }

    public Long lLeftPushIfPresent(String key, Object value) {
        return listOperations.leftPushIfPresent(checkKey(key), value);
    }

    public Long lRightPush(String key, Object value) {
        return listOperations.rightPush(checkKey(key), value);
    }

    public Long lRightPushAll(String key, Object... values) {
        return listOperations.rightPush(checkKey(key), values);
    }

    public Long lRightPushAll(String key, Collection<Object> values) {
        return listOperations.rightPush(checkKey(key), values);
    }

    public Long lRightPushIfPresent(String key, Object value) {
        return listOperations.rightPushIfPresent(checkKey(key), value);

    }

    public Object lIndex(String key, long index) {
        return listOperations.index(getNewKey(key), index);
    }

    public Object lLeftPop(String key) {
        return listOperations.leftPop(getNewKey(key));
    }

    public Object lRightPop(String key) {
        return listOperations.rightPop(getNewKey(key));
    }

    /**
     * =====================================Hash相关操作====================================
     */
    public Long hDelete(String key, String... hashKeys) {
        return hashOperations.delete(getNewKey(key), hashKeys);
    }

    public Boolean hHasKey(String key, String hashKey) {
        return hashOperations.hasKey(getNewKey(key), hashKey);
    }

    public Object hGet(String key, String hashKey) {
        return hashOperations.get(getNewKey(key), hashKey);
    }

    public List<Object> hMultiGet(String key, Collection<String> hashKeys) {
        return hashOperations.multiGet(getNewKey(key), hashKeys);
    }

    public Long hIncrement(String key, String hashKey, long delta) {
        return hashOperations.increment(checkKey(key), hashKey, delta);
    }

    public Double hIncrement(String key, String hashKey, double delta) {
        return hashOperations.increment(checkKey(key), hashKey, delta);
    }

    public Set<String> hKeys(String key) {
        return hashOperations.keys(getNewKey(key));
    }

    public Long hLen(String key) {
        return hashOperations.size(getNewKey(key));
    }

    public void hPutAll(String key, Map<String, Object> m) {
        hashOperations.putAll(checkKey(key), m);
    }

    public void hPut(String key, String hashKey, Object value) {
        hashOperations.put(checkKey(key), hashKey, value);
    }

    public Boolean hPutIfAbsent(String key, String hashKey, Object value) {
        return hashOperations.putIfAbsent(checkKey(key), hashKey, value);
    }

    public List<Object> hValues(String key) {
        return hashOperations.values(getNewKey(key));
    }

    public Map<String, Object> hEntries(String key) {
        return hashOperations.entries(getNewKey(key));
    }

    /**
     * =====================================Set相关操作====================================
     */

    /**
     * =====================================Stored Set相关操作====================================
     */
    public Boolean zAdd(String key, Object member, double score) {
        return zSetOperations.add(key, member, score);
    }

    public Set<Object> zRangeByScore(String key, double score) {
        return zSetOperations.rangeByScore(key, score, score);
    }

    public Set<Object> zRangeLastMember(String key) {
        return zSetOperations.range(key, -1L, -1L);
    }

    public Long zRemRangeByScore(String key, double score) {
        return zSetOperations.removeRangeByScore(key, score, score);
    }

}
