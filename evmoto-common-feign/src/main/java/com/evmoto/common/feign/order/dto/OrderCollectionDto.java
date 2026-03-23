package com.evmoto.common.feign.order.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yelingjun
 * @title: OrderCollectionDto
 * @projectName evmoto-common
 * @description: 订单收入
 * @date 2026/3/19 13:06
 */
@Data
public class OrderCollectionDto implements Serializable {

    /**
     * 司机id
     */
    private Integer driverId;
    /**
     * 订单类型（1=摩托车，4=同城快送）
     */
    private Integer orderType;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 代收费
     */
    private Double collectionFees;
    /**
     * 代收状态（1=未代收，2=已代收）
     */
    private Integer status;
}
