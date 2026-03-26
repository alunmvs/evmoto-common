package com.evmoto.common.mq.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yelingjun
 * @title: AccountBalanceChangeDto
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/24 21:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceChangeDto implements Serializable {

    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 订单类型
     */
    private Integer orderType;
}
