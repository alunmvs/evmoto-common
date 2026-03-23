package com.evmoto.common.feign.near.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yelingjun
 * @title: QueryIdleDriverDto
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/21 09:56
 */
@Data
public class DriverInfoChangeDto implements Serializable {

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 司机所属车型
     */
    private Integer serverCarModelId;

    /**
     * 司机上下班的状态
     */
    private Integer workState;

    /**
     * 司机是否空闲 1=离线，2=空闲，3=服务中
     */
    private Integer state;

    /**
     * 评分
     */
    private Double score;

    /**
     * 账户余额
     */
    private Double amount;

}
