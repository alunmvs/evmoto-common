package com.evmoto.common.redis.able;

import com.evmoto.common.redis.RedissonConstants;
import com.evmoto.common.redis.constant.StrConstants;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * <p> 锁定义接口 </p>
 *
 * @author tanyunpeng
 * @date 2021/11/19
 */
public interface LockInterface {

    /**
     * 限制次数
     */
    String getKey();

    /**
     * 等待时间
     */
    Long getWaitTime();

    /**
     * 等待的时间单位
     * @return
     */
    TimeUnit getTimeUnit();


    default String formatKeyWithParam(String... params) {
        String redisKey = this.getKey();
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; ++i) {
                String target = StrConstants.LEFT_BRACE + i + StrConstants.RIGHT_BRACE;
                if (redisKey.contains(target)) {
                    redisKey = redisKey.replace(target, params[i]);
                }
            }
        }
        if (StringUtils.hasLength(RedissonConstants.NAMESPACE)) {
            return RedissonConstants.NAMESPACE + redisKey;
        }
        return redisKey;
    }
}