package com.evmoto.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yelingjun
 * @title: PrivateCarStatusEnum
 * @projectName evmoto-common
 * @description: 订单状态枚举
 * @date 2026/3/14 14:47
 */
@AllArgsConstructor
public enum PrivateCarStatusEnum {

    PENDING_ACCEPT(1, "待接单"),

    PENDING_DEPARTURE(2, "待出发"),

    PENDING_ARRIVE_AT_RESERVATION_LOCATION(3, "待到达预约地点"),

    WAITING_FOR_PASSENGER_BOARDING(4, "待乘客上车"),

    IN_SERVICE(5, "服务中"),

    SERVICE_COMPLETED(6, "完成服务"),

    PENDING_PAYMENT(7, "待支付"),

    PENDING_EVALUATION(8, "待评价"),

    COMPLETED(9, "已完成"),

    CANCELLED(10, "已取消"),

    REASSIGNING(11, "改派中"),

    CANCELLED_PENDING_PAYMENT(12, "取消待支付");

    ;
    @Getter
    private final int state;

    private final String remark;


    /**
     * 根据状态编码获取枚举对象
     * @param state 状态编码
     * @return 对应的枚举对象，若不存在则返回null
     */
    public static PrivateCarStatusEnum getByCode(int state) {
        for (PrivateCarStatusEnum status : values()) {
            if (status.getState() == state) {
                return status;
            }
        }
        return null;
    }

    /**
     * 安全获取枚举（避免NPE），不存在则抛出异常
     * @param state 状态编码
     * @return 对应的枚举对象
     * @throws IllegalArgumentException 编码不存在时抛出异常
     */
    public static PrivateCarStatusEnum safeGetByCode(int state) {
        PrivateCarStatusEnum status = getByCode(state);
        if (status == null) {
            throw new IllegalArgumentException("无效的订单状态编码：" + state);
        }
        return status;
    }

}
