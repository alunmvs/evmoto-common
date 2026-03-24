package com.evmoto.common.feign.order;

import com.evmoto.common.core.domain.R;
import com.evmoto.common.feign.constant.ServiceNameConstants;
import com.evmoto.common.feign.order.dto.OrderCollectionDto;
import com.evmoto.common.feign.order.dto.OrderHallChangeDto;
import com.evmoto.common.feign.order.vo.OrderPrivateCarVo;
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
}
