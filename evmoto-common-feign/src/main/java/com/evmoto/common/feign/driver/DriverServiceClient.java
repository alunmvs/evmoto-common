package com.evmoto.common.feign.driver;

import com.evmoto.common.core.domain.R;
import com.evmoto.common.feign.constant.ServiceNameConstants;
import com.evmoto.common.feign.driver.vo.DriverInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author yelingjun
 * @title: DriverServiceClient
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/24 22:29
 */
@FeignClient(ServiceNameConstants.MB_CLOUD_DRIVER)
public interface DriverServiceClient {

    @GetMapping("/driver/feign/get/{driverId}")
    R<DriverInfoVo> getByDriverId(@PathVariable("driverId") Integer driverId);
}
