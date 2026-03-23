package com.evmoto.common.feign.pricing;

import com.evmoto.common.core.domain.R;
import com.evmoto.common.feign.constant.ServiceNameConstants;
import com.evmoto.common.feign.pricing.vo.OrderPriceSchemeVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author yelingjun
 * @title: PricingServiceClient
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/17 15:37
 */
@FeignClient(ServiceNameConstants.MB_CLOUD_PRICING)
public interface PricingServiceClient {

    /**
     * 获取价格方案
     * @param priceNo
     * @return
     */
    @GetMapping("/price/scheme/get/{priceNo}")
    R<OrderPriceSchemeVo> getByPriceNo(@PathVariable("priceNo") String priceNo);
}
