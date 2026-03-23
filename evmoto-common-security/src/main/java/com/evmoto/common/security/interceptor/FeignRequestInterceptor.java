package com.evmoto.common.security.interceptor;

import com.evmoto.common.security.constants.SecurityConstants;
import com.evmoto.common.security.context.SecurityContextHolder;
import com.evmoto.common.security.utils.ServletUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author yelingjun
 * @title: FeignRequestInterceptor
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/21 20:02
 */
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
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
        }
    }
}
