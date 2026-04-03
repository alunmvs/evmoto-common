package com.evmoto.common.mq.message;

/**
 * @author yelingjun
 * @title: MessageTopic
 * @projectName evmoto-common
 * @description:
 * @date 2026/4/3 08:22
 */
public interface MessageTopic {

    /**
     * 主题
     */
    String MESSAGE_TOPIC = "push_message_driver_position";

    /**
     * 司机的订单位置
     */
    String DRIVER_POSITION = "driverPosition";

}
