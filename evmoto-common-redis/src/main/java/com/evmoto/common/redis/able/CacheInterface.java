package com.evmoto.common.redis.able;

import com.evmoto.common.redis.RedissonConstants;
import com.evmoto.common.redis.constant.StrConstants;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * <p> 缓存定义接口 </p>
 *
 * @author tanyunpeng
 * @date 2021/11/19
 */
public interface CacheInterface extends Serializable {

    /**
     * 缓存key
     */
    String getKey();

    /**
     * 过期时间
     */
    Duration getExpire();

    /**
     * 描述
     */
    String getDesc();


    default String formatKeyWithParam(boolean needNameSpace, String... params) {
        String redisKey = this.getKey();
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; ++i) {
                String target = StrConstants.LEFT_BRACE + i + StrConstants.RIGHT_BRACE;
                if (redisKey.contains(target)) {
                    redisKey = redisKey.replace(target, params[i]);
                }
            }
        }
        if (needNameSpace && StringUtils.hasLength(RedissonConstants.NAMESPACE)) {
            return RedissonConstants.NAMESPACE + redisKey;
        }
        return redisKey;
    }
}
