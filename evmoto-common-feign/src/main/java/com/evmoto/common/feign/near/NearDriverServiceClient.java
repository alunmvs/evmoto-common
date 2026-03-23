package com.evmoto.common.feign.near;

import com.evmoto.common.core.domain.R;
import com.evmoto.common.feign.constant.ServiceNameConstants;
import com.evmoto.common.feign.near.dto.DriverInfoChangeDto;
import com.evmoto.common.feign.near.dto.QueryIdleDriverDto;
import com.evmoto.common.feign.order.dto.OrderCollectionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author yelingjun
 * @title: NearDriverServiceClient
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/21 09:54
 */
@FeignClient(ServiceNameConstants.MB_CLOUD_NEARDRIVER)
public interface NearDriverServiceClient {

    /**
     * 找出空闲的司机
     * @param dto
     * @return
     */
    @PostMapping("/near/feign/driver/idle")
    R<List<Integer>> queryIdleDriver(@RequestBody QueryIdleDriverDto dto);

    /**
     * 司机的信息修改
     * @param dto
     * @return
     */
    @PostMapping("/near/feign/driver/info/change")
    R<Boolean> driverInfoChange(@RequestBody DriverInfoChangeDto dto);
}
