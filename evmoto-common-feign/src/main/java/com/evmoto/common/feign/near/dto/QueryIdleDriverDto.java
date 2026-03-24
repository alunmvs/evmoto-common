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
public class QueryIdleDriverDto implements Serializable {

    /**
     * 经度
     */
    private Double lon;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 距离
     */
    private Double distance;//KM

    /**
     * 地理围栏
     */
    private String latLon;//开通城市地理围栏

    /**
     * 司机所属车型
     */
    private Integer serverCarModelId;

    /**
     * 司机上下班的状态 1是上班 2是下班
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
     * 余额
     */
    private Double amount;

}
