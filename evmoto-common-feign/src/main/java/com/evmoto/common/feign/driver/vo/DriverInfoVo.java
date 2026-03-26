package com.evmoto.common.feign.driver.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yelingjun
 * @title: DriverInfoVo
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/24 22:30
 */
@Data
public class DriverInfoVo implements Serializable {

    private Integer driverId;

    private Integer carId;

    private Integer serverCarModelId;

    private BigDecimal balance;

    private Integer companyId;
}
