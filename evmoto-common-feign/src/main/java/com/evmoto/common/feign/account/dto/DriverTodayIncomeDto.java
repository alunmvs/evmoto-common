package com.evmoto.common.feign.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yelingjun
 * @title: DriverTodayIncomeDto
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/22 12:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverTodayIncomeDto implements Serializable {

    private Integer driverId;

    private BigDecimal income;
}
