package com.evmoto.common.feign.order;

import com.evmoto.common.core.domain.R;
import com.evmoto.common.feign.constant.ServiceNameConstants;
import com.evmoto.common.feign.order.dto.DriverConfirmFeeDto;
import com.evmoto.common.feign.order.dto.OrderCollectionDto;
import com.evmoto.common.feign.order.dto.OrderHallChangeDto;
import com.evmoto.common.feign.order.vo.OrderPrivateCarVo;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author yelingjun
 * @ClassName: OrderServerClient
 * @projectName evmoto-common
 * @description:
 * @date: 2026/3/19 12:33
 */
@FeignClient(ServiceNameConstants.MB_CLOUD_ORDERSERVER)
public interface OrderServerClient {

    /**
     * 获取专车订单详情
     * @param id
     * @return
     */
    @GetMapping("/order/feign/privatCar/get/{id}")
    R<OrderPrivateCarVo> getPrivateCarById(@PathVariable("id") Long id);

    /**
     * 保存订单的代收数据
     * @param dto
     * @return
     */
    @PostMapping("/order/feign/collect/save")
    R<Boolean> saveOrderCollection(@RequestBody OrderCollectionDto dto);

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @PutMapping("/order/feign/cancel/{orderId}")
    R<Boolean> cancelOrder(@PathVariable("orderId") Long orderId);

    /**
     * 修改订单的orderHall字段
     * @param dto
     * @return
     */
    @PostMapping("/order/feign/hall/change")
    R<Boolean> orderHallChange(@RequestBody OrderHallChangeDto dto);

    /**
     * 抢单操作
     * @param driverId
     * @param orderId
     * @return
     */
    @PutMapping("/order/feign/grab/{driverId}/{orderId}")
    R<Boolean> grabOrder(@PathVariable("driverId") Integer driverId, @PathVariable("orderId") Long orderId);

    /**
     * 还原订单到待接单状态
     * @param orderId
     * @return
     */
    @PutMapping("/order/feign/waitAccept/{orderId}")
    R<Boolean> orderStateToWaitAccept(@PathVariable("orderId") Long orderId);

    /**
     * 乘客取消订单
     * @param orderId
     * @return
     */
    @PutMapping("/order/feign/user/cancel/{userId}/{orderId}")
    R<Boolean> userCancelOrder(@PathVariable("userId") Integer userId, @PathVariable("orderId") Long orderId);

    /**
     * 司机取消订单
     * @param orderId
     * @return
     */
    @PutMapping("/order/feign/driver/cancel/{driverId}/{orderId}")
    R<Boolean> driverCancelOrder(@PathVariable("driverId") Integer driverId, @PathVariable("orderId") Long orderId);

    /**
     * 司机确认费用
     * @param dto
     * @return
     */
    @PostMapping("/order/feign/driver/confirmFee")
    R<Boolean> driverConfirmFee(@RequestBody DriverConfirmFeeDto dto);

    /**
     * 取消待支付（用户取消需支付违约金）
     * @param orderId
     * @return
     */
    @PutMapping("/order/feign/cancelledPendingPayment/{orderId}")
    R<Boolean> cancelledPendingPayment(@PathVariable("orderId") Long orderId);
}
