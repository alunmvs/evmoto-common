package com.evmoto.common.mq.business.topic;

/**
 * @author yelingjun
 * @title: BusinessProcessTopic
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/20 12:39
 */
public interface BusinessProcessTopic {

    /**
     * 主题
     */
    String BUSINESS_PROCESS_TOPIC = "businessProcess";

    /**
     * 订单创建的tag
     */
    String BUSINESS_PROCESS_ORDER_CREATE_TAG = "orderCreate";


    String PUSH_SINGLE_TOPIC = "pushSingle";

    String PUSH_SINGLE_PUSH_ORDER_TAG = "pushTag";
}
