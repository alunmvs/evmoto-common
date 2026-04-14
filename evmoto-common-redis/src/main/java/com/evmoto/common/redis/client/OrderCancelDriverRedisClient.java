package com.evmoto.common.redis.client;

import com.evmoto.common.redis.share.ShareRedisKeyEnum;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yelingjun
 * @title: ShareRedisClient
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/29 20:43
 */
public class OrderCancelDriverRedisClient {

    private RedissonClient redissonClient;

    public OrderCancelDriverRedisClient(EvmotoRedisClient evmotoRedisClient) {
        this.redissonClient = evmotoRedisClient.getRedissonClient();
    }

    private RSet<String> get(Long orderId) {
        String key = ShareRedisKeyEnum.DRIVER_CANCEL_ORDER.formatKeyWithParam(false, orderId.toString());
        RSet<String> set = redissonClient.getSet(key);
        set.expireIfNotSet(ShareRedisKeyEnum.DRIVER_CANCEL_ORDER.getExpire());
        return set;
    }

    public void addDriverId(Long orderId, Integer driverId) {
        RSet<String> set = this.get(orderId);
        set.add(driverId.toString());
    }

    public boolean containsDriverId(Long orderId, Integer driverId) {
        RSet<String> set = this.get(orderId);
        return set.contains(driverId.toString());
    }

    public List<Integer> excludeCancelDriver(Long orderId, List<Integer> list) {
        RSet<String> set = this.get(orderId);
        return list.stream().filter(driverId -> !set.contains(driverId)).collect(Collectors.toList());
    }
}
