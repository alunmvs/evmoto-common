package com.evmoto.common.mq.order.topic;

/**
 * @author yelingjun
 * @ClassName: OrderTopic
 * @projectName evmoto-common
 * @description:
 * @date: 2026/3/29 10:44
 */
public interface OrderServerTopic {

    /**
     * 主题
     */
    String ORDER_SERVER_TOPIC = "orderServer";

    /**
     * 订单状态变更的消息
     */
    String ORDER_SERVER_STATE_CHANGE_TAG = "orderStateChange";
}
