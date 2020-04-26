package com.pan.common.redis;

import com.pan.common.constants.RedisConstants;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */

public class RedisCache<K, V> implements Cache<K, V> {

    private static final String REDIS_SHIRO_CACHE = RedisConstants.REDIS_SHIRO_CACHE;
    private String cacheKey;
    private RedisTemplate<K, V> redisTemplate;

    /**设置30分钟*/
    @Value("${shiro.globalSessionTimeout}")
    private Long globExpire;

    @SuppressWarnings("rawtypes")
    public RedisCache(String name, RedisTemplate client) {
        this.cacheKey = REDIS_SHIRO_CACHE + name + ":";
        this.redisTemplate = client;
    }

    @Override
    public V get(K key) throws CacheException {
        V value = redisTemplate.boundValueOps(getCacheKey(key)).get();
        if(value!=null){
            //毫秒
            redisTemplate.boundValueOps(getCacheKey(key)).expire(globExpire, TimeUnit.MILLISECONDS);
        }
        return value;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        V old = get(key);
        redisTemplate.boundValueOps(getCacheKey(key)).set(value);
        //毫秒
        redisTemplate.boundValueOps(getCacheKey(key)).expire(globExpire, TimeUnit.MILLISECONDS);
        return old;
    }

    @Override
    public V remove(K key) throws CacheException {
        V old = get(key);
        redisTemplate.delete(getCacheKey(key));
        return old;
    }

    @Override
    public void clear() throws CacheException {
        redisTemplate.delete(keys());
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Set<K> keys() {
        return redisTemplate.keys(getCacheKey("*"));
    }

    /**
     * 获取所有的value
     */
    @Override
    public Collection<V> values() {
        Set<K> keys = this.keys();

        List<V> list = new ArrayList<>();
        for (K key : keys) {
            list.add(get(key));
        }
        return list;
    }

    private K getCacheKey(Object k) {
        return (K) (this.cacheKey + k);
    }
}
