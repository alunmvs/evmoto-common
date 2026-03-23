package com.evmoto.common.redis.util;

import com.evmoto.common.redis.RedissonConstants;
import com.evmoto.common.redis.able.CacheInterface;
import com.evmoto.common.redis.able.LockInterface;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RExpirable;
import org.redisson.api.RLock;
import org.springframework.util.StringUtils;

@Slf4j
public class RedissonUtils {

    /**
     * 获取缓存key
     */
    public static String getCacheKey(CacheInterface cacheInterface, String key) {
        String cacheKey = cacheInterface.getKey();
        if (StringUtils.hasLength(RedissonConstants.NAMESPACE)) {
            cacheKey = RedissonConstants.NAMESPACE.concat(cacheInterface.getKey());
        }
        if (!StringUtils.hasLength(key)) {
            return cacheKey;
        }
        if (cacheInterface.getKey().endsWith(RedissonConstants.SEPARATOR)) {
            cacheKey = cacheKey.concat(key);
        } else {
            cacheKey = cacheKey.concat(RedissonConstants.SEPARATOR).concat(key);
        }
        return cacheKey;
    }

    /**
     * 设置缓存过期时间
     */
    public static boolean expire(RExpirable rExpirable, CacheInterface cacheInterface) {
        //return rExpirable.expire(cacheInterface.getExpire(), cacheInterface.getUnit());
        return false;
    }

    /**
     * 尝试加锁
     */
    public static boolean tryLock(RLock rLock, LockInterface lockInterface) {
        /*try {
            return rLock.tryLock(lockInterface.getWaitTime(), lockInterface.getExpire(), lockInterface.getUnit());
        } catch (InterruptedException e) {
            log.error(" tryLock error, lock:{}", rLock.getName(), e);
        }*/
        return false;
    }

}
