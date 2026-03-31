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
        //evmotoRedisClient.setNoNameSpace(ShareRedisKeyEnum.DRIVER_WORK, driverWork, DriverWorkBo.class, driverId.toString());
        addToRedis(driverId, driverWork);
    }

    /**
     * 删除司机等待确认支付的标志
     * @param driverId
     */
    public void delDriverWaitPayConfirm(Integer driverId) {
        if (driverId == null) {
            return;
        }
        DriverWorkBo driverWork = getDriverWork(driverId);
        driverWork.setWaitPayConfirmOrderId(null);
        driverWork.setWaitPayConfirm(0);
        //evmotoRedisClient.setNoNameSpace(ShareRedisKeyEnum.DRIVER_WORK, driverWork, DriverWorkBo.class, driverId.toString());
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

    public void clearDriverWorkOrderId(Integer driverId) {
        DriverWorkBo driverWork = getDriverWork(driverId);
        driverWork.setOrderId(null);
        addToRedis(driverId, driverWork);
    }

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
