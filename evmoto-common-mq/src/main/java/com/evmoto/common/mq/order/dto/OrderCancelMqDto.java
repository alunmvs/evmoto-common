package com.evmoto.common.mq.order.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yelingjun
 * @title: OrderCancelMqDto
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/31 12:39
 */
@Data
public class OrderCancelMqDto implements Serializable {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 取消人id
     */
    private Integer operateUserId;

    /**
     * 取消人的是否 user:乘客 driver:司机
     */
    private String cancelUserType;
}
