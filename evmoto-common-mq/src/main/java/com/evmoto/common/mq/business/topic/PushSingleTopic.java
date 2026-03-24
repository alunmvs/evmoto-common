package com.evmoto.common.mq.business.topic;

/**
 * @author yelingjun
 * @title: BusinessProcessTopic
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/20 12:39
 */
public interface PushSingleTopic {

    /**
     * 主题
     */
    String PUSH_SINGLE_TOPIC = "pushSingle";

    /**
     * 推送
     */
    String PUSH_SINGLE_PUSH_ORDER_TAG = "pushTag";
}
