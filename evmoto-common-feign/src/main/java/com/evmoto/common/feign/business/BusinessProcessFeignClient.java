package com.evmoto.common.feign.business;

import com.evmoto.common.core.domain.R;
import com.evmoto.common.feign.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author yelingjun
 * @title: BusinessProcessFeignClient
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/31 16:12
 */
@FeignClient(ServiceNameConstants.MB_CLOUD_BUSINESSPROCESS)
public interface BusinessProcessFeignClient {

    /**
     * 推单
     * @param orderId
     * @return
     */
    @PutMapping("/business/feign/push/order/{orderId}")
    R<Boolean> pushOrder(@PathVariable("orderId") Long orderId);
}
