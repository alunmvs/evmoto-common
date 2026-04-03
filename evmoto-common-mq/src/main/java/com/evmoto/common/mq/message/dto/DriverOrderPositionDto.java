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
     * 乘客id
     */
    private Integer userId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 当前坐标
     */
    private Double lat;

    /**
     * 当前坐标
     */
    private Double lon;

    /**
     * 上一次的坐标
     */
    private Double preLat;

    /**
     * 上一次的坐标
     */
    private Double preLon;
}
