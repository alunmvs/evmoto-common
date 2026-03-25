package com.evmoto.common.feign.app;

import com.evmoto.common.core.domain.R;
import com.evmoto.common.feign.constant.ServiceNameConstants;
import com.evmoto.common.feign.order.dto.OrderCollectionDto;
import com.evmoto.common.feign.order.vo.OrderPrivateCarVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

/**
 * @author yelingjun
 * @ClassName: OrderServerClient
 * @projectName evmoto-common
 * @description:
 * @date: 2026/3/19 12:33
 */
@FeignClient(ServiceNameConstants.MB_CLOUD_APP)
public interface AppServerClient {

    /**
     * 获取司机上班的最低金额
     * @return
     */
    @GetMapping("/app/feign/minMoney/get")
    R<BigDecimal> getMinMoney();

    /**
     * 获取车型信息
     * @return
     */
    @GetMapping("/app/feign/carModel/get/{orderType}/{carId}")
    R<Integer> getServerCarModelId(@PathVariable("carId") Integer orderType,
                                   @PathVariable("carId") Integer carId);

}
