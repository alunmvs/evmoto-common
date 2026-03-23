package com.evmoto.common.feign.pricing.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yelingjun
 * @title: OrderPriceSchemeVo
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/17 16:00
 */
@Data
public class OrderPriceSchemeVo implements Serializable {

    /**
     * 起步里程
     */
    private Double startMileage = 0D;//
    /**
     * 起步金额
     */
    private Double startMoney = 0D;//起步金额
    /**
     * 起步时长
     */
    private Double startTime = 0D;//起步时长
    /**
     * 里程公里
     */
    private Double mileageKilometers = 0D;//里程公里
    /**
     * 里程费
     */
    private Double mileageMoney = 0D;//里程费
    /**
     * 时长分钟
     */
    private Double duration = 0D;//时长分钟
    /**
     * 时长费
     */
    private Double durationMoney = 0D;//时长费
    /**
     * 等待分钟
     */
    private Double wait = 0D;//等待分钟
    /**
     * 等待费
     */
    private Double waitMoney = 0D;//等待费
    /**
     * 高峰期费
     */
    private Double fastigiumMoney = 0D;//高峰期费
    /**
     * 远途公里
     */
    private Double longDistance = 0D;//远途公里
    /**
     * 远途费
     */
    private Double longDistanceMoney = 0D;//远途费
    /**
     * 远途费
     */
    private Double nightMoney = 0D;//夜间费
    /**
     * 订单金额
     */
    private Double orderMoney = 0D;//订单金额

    /**
     * 起点坐标
     */
    private String startLonLat;

    /**
     * 终点坐标
     */
    private String endLonLat;
}
