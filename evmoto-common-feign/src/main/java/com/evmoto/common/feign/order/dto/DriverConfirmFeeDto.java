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
}
