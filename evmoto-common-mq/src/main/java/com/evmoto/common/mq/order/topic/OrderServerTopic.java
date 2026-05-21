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

    /**
     * 订单取消的mq
     */
    String ORDER_SERVER_ORDER_CANCEL = "orderCancel";

    /**
     * 司机取消订单后，等待用户选择超时的mq（delay message）
     * Driver cancel popup timeout: auto-cancel if user doesn't respond within 1 minute
     */
    String ORDER_SERVER_DRIVER_CANCEL_TIMEOUT = "driverCancelTimeout";
}
