package com.evmoto.common.mq.business.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yelingjun
 * @title: OrderCreateTagDto
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/20 12:39
 */
@Data
public class OrderCreateTagDto implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 公司id
     */
    private Integer companyId;

    /**
     * 乘客
     */
    private Integer userId;

    /**
     * 司机
     */
    private Integer driverId;

    /**
     * 下单选择的服务车型
     */
    private String serverCarModelIdStr;

    /**
     * 起点经度
     */
    private Double startLon;

    /**
     * 起点纬度
     */
    private Double startLat;

    /**
     * 终点经度
     */
    private Double endLon;

    /**
     * 终点经度
     */
    private Double endLat;
}
