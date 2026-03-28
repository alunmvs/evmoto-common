package com.evmoto.common.feign.order;

import com.evmoto.common.core.domain.R;
import com.evmoto.common.feign.constant.ServiceNameConstants;
import com.evmoto.common.feign.order.dto.DriverGeoQueryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author yelingjun
 * @title: DriverGeoService
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/28 12:41
 */
@FeignClient(ServiceNameConstants.MB_CLOUD_ORDERSERVER)
public interface DriverGeoService {

    /**
     * 司机查询
     * @param dto
     * @return
     */
    @PostMapping("/geo/feign/driver/query")
    R<List<Integer>> queryDriver(@RequestBody DriverGeoQueryDto dto);
}
