package com.evmoto.common.mq.message.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yelingjun
 * @title: DriverOrderPostion
 * @projectName evmoto-common
 * @description:
 * @date 2026/4/3 08:23
 */
@Data
public class DriverOrderPositionDto implements Serializable {

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单类型
     */
    private Integer orderType;


    private Double lat;

    private Double lon;
}
