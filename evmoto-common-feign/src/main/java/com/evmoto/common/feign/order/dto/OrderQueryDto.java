package com.evmoto.common.feign.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yelingjun
 * @title: OrderQueryDto
 * @projectName evmoto-common
 * @description:
 * @date 2026/4/6 19:15
 */
@Data
public class OrderQueryDto implements Serializable {

    private Integer driverId;

    private List<Integer> state;

    private Integer orderType;
}
