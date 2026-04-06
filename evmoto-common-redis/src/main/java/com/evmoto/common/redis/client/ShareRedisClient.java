package com.evmoto.common.redis.client;

import com.evmoto.common.redis.share.ShareRedisKeyEnum;
import com.evmoto.common.redis.share.bo.DriverWorkBo;

/**
 * @author yelingjun
 * @title: ShareRedisClient
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/29 20:43
 */
public class ShareRedisClient {

    private EvmotoRedisClient evmotoRedisClient;

    public ShareRedisClient(EvmotoRedisClient evmotoRedisClient) {
        this.evmotoRedisClient = evmotoRedisClient;
    }

    /**
     * 设置司等待确认支付的标志位
     * @param driverId
     */
    public void setDriverWaitPayConfirm(Integer driverId, Long orderId) {
        if (driverId == null) {
            return;
        }
        DriverWorkBo driverWork = getDriverWork(driverId);
        driverWork.setWaitPayConfirmOrderId(orderId);
        driverWork.setWaitPayConfirm(1);
        addToRedis(driverId, driverWork);
    }

    /**
     * 删除司机等待确认支付的标志
     * @param driverId
     */
    public void clearDriverWaitPayConfirm(Integer driverId) {
        if (driverId == null) {
            return;
        }
        DriverWorkBo driverWork = getDriverWork(driverId);
        driverWork.setWaitPayConfirmOrderId(null);
        driverWork.setWaitPayConfirm(0);
        addToRedis(driverId, driverWork);
    }

    /**
     * 判断司机是否存在等待确认支付的标志
     * @param driverId
     */
    public boolean existDriverWaitPayConfirm(Integer driverId) {
        if (driverId == null) {
            return false;
        }
        DriverWorkBo driverWork = getDriverWork(driverId);
        Integer waitPayConfirm = driverWork.getWaitPayConfirm();
        return waitPayConfirm != null && waitPayConfirm == 1 ? true : false;
    }

    /**
     * 是否存在工作中的订单
     * @param driverId
     * @return
     */
    public boolean existDriverWorkOrder(Integer driverId) {
        if (driverId == null) {
            return false;
        }
        DriverWorkBo driverWork = getDriverWork(driverId);
        return driverWork.getOrderId() == null ? false :true;
    }

    /**
     * 设置设置目前在服务的订单
     * @param driverId
     * @param orderId
     */
    public void setWorkOrderId(Integer driverId, Long orderId) {
        DriverWorkBo driverWork = getDriverWork(driverId);
        driverWork.setOrderId(orderId);
        addToRedis(driverId, driverWork);
    }

    /**
     * 获取司机当前正在服务的订单id
     * @param driverId
     * @return
     */
    public Long getDriverWorkOrderId(Integer driverId) {
        DriverWorkBo driverWork = getDriverWork(driverId);
        return driverWork.getOrderId();
    }

    /**
     * 清空司机工作中的订单
     * @param driverId
     */
    public void clearDriverWorkOrderId(Integer driverId) {
        DriverWorkBo driverWork = getDriverWork(driverId);
        driverWork.setOrderId(null);
        addToRedis(driverId, driverWork);
    }

    /**
     * 清空所有标志位
     * @param driverId
     */
    public void clearAllDriverWork(Integer driverId) {
        DriverWorkBo driverWork = getDriverWork(driverId);
        driverWork.setOrderId(null);
        driverWork.setWaitPayConfirm(null);
        driverWork.setWaitPayConfirmOrderId(null);
        addToRedis(driverId, driverWork);
    }

    /**
     * 设置工作中的订单id
     * @param driverId
     * @param orderId
     */
    public void setAllDriverWork(Integer driverId, Long orderId) {
        DriverWorkBo driverWork = getDriverWork(driverId);
        driverWork.setOrderId(orderId);
        driverWork.setWaitPayConfirm(1);
        driverWork.setWaitPayConfirmOrderId(orderId);
        addToRedis(driverId, driverWork);
    }

    /**
     * 获取
     * @param driverId
     * @return
     */
    private DriverWorkBo getDriverWork(Integer driverId) {
        DriverWorkBo driverWork = evmotoRedisClient.getNoNameSpace(ShareRedisKeyEnum.DRIVER_WORK, DriverWorkBo.class, driverId.toString());
        if (driverWork == null) {
            driverWork = new DriverWorkBo();
            driverWork.setDriverId(driverId);
        }
        return driverWork;
    }

    private void addToRedis(Integer driverId, DriverWorkBo driverWork) {
        evmotoRedisClient.setNoNameSpace(ShareRedisKeyEnum.DRIVER_WORK, driverWork, DriverWorkBo.class, driverId.toString());
    }
}
