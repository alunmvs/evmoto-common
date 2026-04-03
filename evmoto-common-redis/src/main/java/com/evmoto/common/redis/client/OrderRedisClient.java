package com.evmoto.common.redis.client;

import com.evmoto.common.redis.share.ShareRedisKeyEnum;
import com.evmoto.common.redis.share.bo.DriverWorkBo;
import com.evmoto.common.redis.share.bo.OrderBo;

/**
 * @author yelingjun
 * @title: ShareRedisClient
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/29 20:43
 */
public class OrderRedisClient {

    private EvmotoRedisClient evmotoRedisClient;

    public OrderRedisClient(EvmotoRedisClient evmotoRedisClient) {
        this.evmotoRedisClient = evmotoRedisClient;
    }

    /**
     * 初始化一个订单进缓存
     * @param orderId
     * @param userId
     * @param state
     */
    public void initOrder(Long orderId, Integer userId, Integer state) {
        OrderBo order = new OrderBo();
        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setState(state);
        addToRedis(orderId, order);
    }

    /**
     * 初始化一个订单进缓存
     * @param order
     */
    public void initOrder(OrderBo order) {
        addToRedis(order.getOrderId(), order);
    }

    public Integer getOrderStateByOrderId(Long orderId) {
        OrderBo order = getOrder(orderId);
        if (order == null) {
            return null;
        }
        return order.getState();
    }

    public void setStateByOrderId(Long orderId, Integer state) {
        OrderBo order = getOrder(orderId);
        if (order == null) {
            order = new OrderBo();
        }
        order.setOrderId(orderId);
        order.setState(state);
        addToRedis(orderId, order);
    }

    /**
     * 获取
     * @param orderId
     * @return
     */
    public OrderBo getOrder(Long orderId) {
        OrderBo order = evmotoRedisClient.getNoNameSpace(ShareRedisKeyEnum.ORDER, OrderBo.class, orderId.toString());
        return order;
    }

    private void addToRedis(Long orderId, OrderBo order) {
        evmotoRedisClient.setNoNameSpace(ShareRedisKeyEnum.ORDER, order, OrderBo.class, orderId.toString());
    }
}
