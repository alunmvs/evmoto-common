package com.evmoto.common.feign.order.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yelingjun
 * @title: OrderPrivateCarVo
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/19 12:35
 */
@Data
public class OrderPrivateCarVo implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 订单状态  1=待接单，2=待出发，3=待到达预约地点，4=待乘客上车，5=服务中，6=完成服务，7=待支付，8=待评价，9=已完成，10=已取消，11=改派中，12=取消待支付
     */
    private Integer state;

    /**
     * 订单类型（1=普通订单，2=摆渡订单）1=Regular order, 2=Shuttle order.
     */
    private Integer type;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 订单号
     */
    private String orderNum;

    /**
     * 订单金额
     */
    private Double orderMoney;

    /**
     * 起步价
     */
    private Double startMoney;

    /**
     * 里程费
     */
    private Double mileageMoney;

    /**
     * 时长费
     */
    private Double durationMoney;

    /**
     * 等待费
     */
    private Double waitMoney;

    /**
     * 远途费
     */
    private Double longDistanceMoney;

    /**
     * 高峰费
     */
    private Double fastigiumMoney;

    /**
     * 夜间费
     */
    private Double nightMoney;

    /**
     * 附加费
     */
    private Double additionalCharge;

    /**
     * 优惠券抵扣金额
     */
    private Double couponMoney;

    /**
     * 优惠券抵扣金额
     */
    private Integer couponId;

    /**
     * 公司id
     */
    private Integer companyId;


}


