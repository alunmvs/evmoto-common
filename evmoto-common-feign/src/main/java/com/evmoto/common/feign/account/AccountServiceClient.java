package com.evmoto.common.feign.account;

import com.evmoto.common.core.domain.R;
import com.evmoto.common.feign.account.dto.DriverTodayIncomeDto;
import com.evmoto.common.feign.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author yelingjun
 * @title: AccountServiceClient
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/22 12:15
 */
@FeignClient(ServiceNameConstants.MB_CLOUD_ACCOUNT)
public interface AccountServiceClient {

    /**
     * 获取当天收入
     * @param driverIds
     * @return
     */
    @PostMapping("/account/feign/todayIncome/get")
    R<List<DriverTodayIncomeDto>> getDriverTodayIncome(@RequestBody List<Integer> driverIds);

    /**
     * 用户确认支付
     * @param orderId
     * @return
     */
    @PutMapping("/account/feign/user/pay/confirm/{orderId}")
    R<Boolean> userConfirm(@PathVariable("orderId") Long orderId);
}
