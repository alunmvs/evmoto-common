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

    PUSH_ORDER_DRIVER_CONFIRM("share:push:orderConfirm:{0}:{1}", Duration.ofMinutes(10), "推送订单，司机确认标志"),

    DRIVER_POSITION("share:position:{0}", Duration.ofSeconds(30), "司机实时位置"),

    DRIVER_STATE("share:position:{0}", Duration.ofHours(8), "司机的服务状态"),

    DRIVER_PAY_CONFIRM("share:position:{0}", Duration.ofDays(1), "司机有支付为确认的状态标志"),

    DRIVER_WORK("share:work:{0}", Duration.ofDays(10), "司机各种状态"),

    ORDER_STATE("share:orderState:{0}", Duration.ofDays(1), "订单的状态共享"),

    ORDER("share:order:{0}", Duration.ofDays(5), "订单信息"),

    ORDER_TRACK("share:order_track:{0}", Duration.ofDays(3), "订单轨迹"),
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
