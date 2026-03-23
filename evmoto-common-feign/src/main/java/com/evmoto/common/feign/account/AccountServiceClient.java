package com.evmoto.common.feign.account;

import com.evmoto.common.core.domain.R;
import com.evmoto.common.feign.account.dto.DriverTodayIncomeDto;
import com.evmoto.common.feign.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author yelingjun
 * @title: AccountServiceClient
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/22 12:15
 */
@FeignClient(ServiceNameConstants.MB_CLOUD_ORDERSERVER)
public interface AccountServiceClient {

    @PostMapping("/account/feign/todayIncome/get")
    R<List<DriverTodayIncomeDto>> getDriverTodayIncome(@RequestBody List<Integer> driverIds);

}
