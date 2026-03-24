package com.evmoto.common.feign.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yelingjun
 * @title: OrderHallChangeDto
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/24 10:39
 */
@Data
@AllArgsConstructor
public class OrderHallChangeDto implements Serializable {

    private Long orderId;

    /**
     *  1=正常推单，2=抢单大厅
     */
    private Integer hall;
}
