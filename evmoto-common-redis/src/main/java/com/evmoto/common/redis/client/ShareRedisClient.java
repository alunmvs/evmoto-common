package com.evmoto.common.redis.client;

import com.evmoto.common.redis.share.ShareRedisKeyEnum;
import org.redisson.api.RedissonClient;

/**
 * @author yelingjun
 * @title: ShareRedisClient
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/29 20:43
 */
public class ShareRedisClient {

    private EvmotoRedisClient evmotoRedisClient;

    public ShareRedisClient(EvmotoRedisClient evmotoRedisClient) {
        this.evmotoRedisClient = evmotoRedisClient;
    }

    /**
     * 设置司等待确认支付的标志位
     * @param driverId
     */
    public void setDriverWaitPayConfirm(Integer driverId, Long orderId) {
        if (driverId == null) {
            return;
        }
        evmotoRedisClient.setNoNameSpace(ShareRedisKeyEnum.DRIVER_PAY_CONFIRM, orderId, Long.class, driverId.toString());
    }

    /**
     * 删除司机等待支付的标志
     * @param driverId
     */
    public void delDriverWaitPayConfirm(Integer driverId) {
        if (driverId == null) {
            return;
        }
        evmotoRedisClient.deleteNoNameSpace(ShareRedisKeyEnum.DRIVER_PAY_CONFIRM, driverId.toString());
    }

    /**
     * 删除司机等待支付的标志
     * @param driverId
     */
    public boolean existDriverWaitPayConfirm(Integer driverId) {
        if (driverId == null) {
            return false;
        }
        return evmotoRedisClient.exist(ShareRedisKeyEnum.DRIVER_PAY_CONFIRM, driverId.toString());
    }
}
