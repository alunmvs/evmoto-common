package com.evmoto.common.mq.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yelingjun
 * @title: OrderStageChangeDto
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/29 10:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStageChangeDto implements Serializable {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 旧的订单状态
     */
    private int oldState;

    /**
     * 当前订单状态
     */
    private int currentState;
}
