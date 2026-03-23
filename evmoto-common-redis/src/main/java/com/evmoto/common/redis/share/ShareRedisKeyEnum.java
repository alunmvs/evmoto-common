package com.evmoto.common.redis.share;

import com.evmoto.common.redis.able.CacheInterface;
import lombok.AllArgsConstructor;

import java.time.Duration;

/**
 * @author yelingjun
 * @ClassName: ShareRedisKeyEnum
 * @projectName evmoto-common
 * @description:
 * @date: 2026/3/23 09:41
 */
@AllArgsConstructor
public enum ShareRedisKeyEnum implements CacheInterface {

    SOCKET_DRIVER("share:socket:driver:{0}", Duration.ofSeconds(30), "司机的socket标志"),

    SOCKET_USER("share:socket:user:{0}", Duration.ofMinutes(30), "乘客的socket标志"),
    ;

    private String key;

    private Duration expire;

    private String desc;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Duration getExpire() {
        return expire;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
