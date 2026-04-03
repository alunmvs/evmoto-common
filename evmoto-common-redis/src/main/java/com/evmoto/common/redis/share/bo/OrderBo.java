package com.evmoto.common.redis.share.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yelingjun
 * @title: OrderBo
 * @projectName evmoto-common
 * @description:
 * @date 2026/4/3 08:29
 */
@Data
public class OrderBo implements Serializable {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 乘客id
     */
    private Integer userId;

    /**
     * 订单状态
     */
    private Integer state;

    /**
     * 上车点坐标
     */
    private double startLon;

    /**
     * 上车点坐标
     */
    private double startLat;

    /**
     * 下车点坐标
     */
    private double endLon;

    /**
     * 下车点坐标
     */
    private double endLat;
}
