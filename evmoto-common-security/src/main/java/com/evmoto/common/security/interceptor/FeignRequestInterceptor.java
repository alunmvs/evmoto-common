package com.evmoto.common.security.interceptor;

import com.evmoto.common.security.constants.SecurityConstants;
import com.evmoto.common.security.context.SecurityContextHolder;
import com.evmoto.common.security.utils.ServletUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author yelingjun
 * @title: FeignRequestInterceptor
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/21 20:02
 */
@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class FeignRequestInterceptor implements RequestInterceptor {

    public FeignRequestInterceptor() {
        log.info("---------> FeignRequestInterceptor init success");
    }
    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("开始拦截feign的请求了");
        HttpServletRequest httpServletRequest = ServletUtils.getRequest();
        if (httpServletRequest != null) {
            Map<String, String> headers = ServletUtils.getHeaders(httpServletRequest);
            String role = headers.get(SecurityConstants.ROLE);
            if (StringUtils.isNotEmpty(role)) {
                requestTemplate.header(SecurityConstants.ROLE, role);
            }
            String deviceId = headers.get(SecurityConstants.DEVICE_ID);
            if (StringUtils.isNotEmpty(deviceId)) {
                requestTemplate.header(SecurityConstants.DEVICE_ID, deviceId);
            }
            String authorization = headers.get(SecurityConstants.AUTHORIZATION);
            if (StringUtils.isNotEmpty(role)) {
                requestTemplate.header(SecurityConstants.AUTHORIZATION, authorization);
            }
            String clientId = headers.get("clientId");
            if (StringUtils.isNotEmpty(role)) {
                requestTemplate.header("clientId", clientId);
            }
        } else {
            String role = SecurityContextHolder.getRole();
            if (StringUtils.isNotEmpty(role)) {
                requestTemplate.header(SecurityConstants.ROLE, role);
            }
            String deviceId = SecurityContextHolder.getDeviceId();
            if (StringUtils.isNotEmpty(deviceId)) {
                requestTemplate.header(SecurityConstants.DEVICE_ID, deviceId);
            }
            String authorization = SecurityContextHolder.getAuthorization();
            if (StringUtils.isNotEmpty(role)) {
                requestTemplate.header(SecurityConstants.AUTHORIZATION, authorization);
            }
            if (SecurityContextHolder.getSafeUserId() != null) {
                requestTemplate.header("clientId", SecurityContextHolder.getSafeUserId().toString());
            } else if (SecurityContextHolder.getSafeDriverId() != null) {
                requestTemplate.header("clientId", SecurityContextHolder.getSafeDriverId().toString());
            }

        }
    }
}
