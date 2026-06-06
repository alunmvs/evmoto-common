package com.evmoto.common.feign.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yelingjun
 * @title: DriverConfirmFeeDto
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/31 16:41
 */
@Data
public class DriverConfirmFeeDto implements Serializable {

    private Long orderId;

    private Double additionalCharge;

    private String surchargeDescription;

    private Date driverConfirmFeesAt;

    private Double wait;

    private Double waitMoney;

    private Double driverWait;

    private Double driverWaitMoney;

    private Double orderMoney;

    private Double payMoney;

    private Double driverOrderMoney;

    // User-side pricing breakdown (final values from confirmFees)
    private Double startMileage;
    private Double startMoney;
    private Double mileageKilometers;
    private Double mileageMoney;
    private Double duration;
    private Double durationMoney;
    private Double fastigiumMoney;
    private Double nightMoney;
    private Double longDistance;
    private Double longDistanceMoney;

    // Driver-side pricing breakdown (final values from confirmFees)
    private Double driverStartMileage;
    private Double driverStartMoney;
    private Double driverMileageKilometers;
    private Double driverMileageMoney;
    private Double driverDuration;
    private Double driverDurationMoney;
    private Double driverFastigiumMoney;
    private Double driverNightMoney;
    private Double driverLongDistance;
    private Double driverLongDistanceMoney;

    private Double serviceCharge;
    private Double collectionFees;
}
