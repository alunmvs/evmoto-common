package com.evmoto.common.redis.share.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yelingjun
 * @title: DriverWorkBo
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/30 12:29
 */
@Data
public class DriverWorkBo implements Serializable {

    /**
     * 司机信息
     */
    private Integer driverId;

    /**
     * 当前服务的订单，为null就表示没有
     */
    private Long orderId;

    /**
     * 支付待确认标志 1表示有  0：表示没有
     */
    private Integer waitPayConfirm;

    /**
     * 司机待确认支付的订单id
     */
    private Long waitPayConfirmOrderId;

}
