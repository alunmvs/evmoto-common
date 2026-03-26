package com.evmoto.common.feign.message;

/**
 * @author yelingjun
 * @title: PushMessageServiceClient
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/25 19:57
 */

import com.evmoto.common.core.domain.R;
import com.evmoto.common.feign.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author yelingjun
 * @title: DriverServiceClient
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/24 22:29
 */
@FeignClient(ServiceNameConstants.MB_CLOUD_PUSHMESSAGE)
public interface PushMessageServiceClient {

    /**
     * 推送下班消息给司机
     * @param driverId
     * @return
     */
    @PutMapping("/push/message/offWork/{driverId}")
    R<Boolean> pushOffWorkToDriver(@PathVariable("driverId") Integer driverId);
}
