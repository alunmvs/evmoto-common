package com.evmoto.common.redis.client;

import com.evmoto.common.redis.able.CacheInterface;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.TypedJsonJacksonCodec;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * @author yelingjun
 * @title: EvmotoRedisClient
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/17 15:03
 */
public class EvmotoRedisClient {

    private RedissonClient redissonClient;

    private EvmotoRedisClient() {}

    public EvmotoRedisClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public <V> V getNoNameSpace(CacheInterface keyEnum, Class<V> valueClass, Supplier<V> supplier, String... params) {
        return get(keyEnum, false, valueClass, supplier, params);
    }

    public <V> V get(CacheInterface keyEnum, Class<V> valueClass, Supplier<V> supplier, String... params) {
        return get(keyEnum, true, valueClass, supplier, params);
    }

    private <V> V get(CacheInterface keyEnum, boolean needNameSpace, Class<V> valueClass, Supplier<V> supplier, String... params) {
        String key = keyEnum.formatKeyWithParam(needNameSpace, params);
        Codec codec = new TypedJsonJacksonCodec(valueClass);
        RBucket<V> bucket = redissonClient.getBucket(key, codec);
        V obj = bucket.get();
        if (obj == null && supplier != null) {
            obj = supplier.get();
            if (obj != null) {
                bucket.set(obj, keyEnum.getExpire());
            }
        }
        return obj;
    }

    public <V> V get(CacheInterface keyEnum, Class<V> valueClass, String... params) {
        return this.get(keyEnum, true, valueClass, null, params);
    }

    public <V> V getNoNameSpace(CacheInterface keyEnum, Class<V> valueClass, String... params) {
        return this.get(keyEnum, false, valueClass, null, params);
    }

    private <V> void set(CacheInterface keyEnum, boolean needNameSpace, V obj, Class<V> valueClass, Duration expire, String... params) {
        String key = keyEnum.formatKeyWithParam(needNameSpace, params);
        Codec codec = new TypedJsonJacksonCodec(valueClass);
        RBucket<V> bucket = redissonClient.getBucket(key, codec);
        bucket.set(obj, expire == null ? keyEnum.getExpire() : expire);
    }

    /**
     * 设置值
     * @param keyEnum
     * @param obj
     * @param valueClass
     * @param params
     * @param <V>
     */
    public  <V> void set(CacheInterface keyEnum, V obj, Class<V> valueClass, String... params) {
        /*String key = keyEnum.formatKeyWithParam(true, params);
        Codec codec = new TypedJsonJacksonCodec(valueClass);
        RBucket<V> bucket = redissonClient.getBucket(key, codec);
        bucket.set(obj, keyEnum.getExpire());*/
        set(keyEnum, true, obj, valueClass, null, params);
    }

    /**
     * 设置值，不需要带nameSpace
     * @param keyEnum
     * @param obj
     * @param valueClass
     * @param params
     * @param <V>
     */
    public <V> void setNoNameSpace(CacheInterface keyEnum, V obj, Class<V> valueClass, String... params) {
        /*String key = keyEnum.formatKeyWithParam(false, params);
        Codec codec = new TypedJsonJacksonCodec(valueClass);
        RBucket<V> bucket = redissonClient.getBucket(key, codec);
        bucket.set(obj, keyEnum.getExpire());*/
        set(keyEnum, false, obj, valueClass, null, params);
    }

    /**
     * 设置值，自定义过期时间
     * @param keyEnum
     * @param obj
     * @param valueClass
     * @param params
     * @param <V>
     */
    public  <V> void set(CacheInterface keyEnum, V obj, Class<V> valueClass, Duration expire, String... params) {
        set(keyEnum, true, obj, valueClass, expire, params);
    }

    /**
     * 设置值，不需要带nameSpace 自定义过期时间
     * @param keyEnum
     * @param obj
     * @param valueClass
     * @param params
     * @param <V>
     */
    public <V> void setNoNameSpace(CacheInterface keyEnum, V obj, Class<V> valueClass, Duration expire, String... params) {
        set(keyEnum, false, obj, valueClass, expire, params);
    }

    public void delete(CacheInterface keyEnum, String... params) {
        String key = keyEnum.formatKeyWithParam(true, params);
        redissonClient.getBucket(key).delete();
    }

    public void deleteNoNameSpace(CacheInterface keyEnum, String... params) {
        String key = keyEnum.formatKeyWithParam(false, params);
        redissonClient.getBucket(key).delete();
    }

    public boolean exist(CacheInterface keyEnum, String... params) {
        return exist(keyEnum, true, params);
    }

    private boolean exist(CacheInterface keyEnum, boolean needNameSpace, String... params) {
        String key = keyEnum.formatKeyWithParam(needNameSpace, params);
        return redissonClient.getBucket(key).isExists();
    }

    public boolean existNoNameSpace(CacheInterface keyEnum, String... params) {
        return exist(keyEnum, false, params);
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }
}
