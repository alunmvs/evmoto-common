package com.evmoto.common.security.interceptor;

import com.evmoto.common.security.constants.SecurityConstants;
import com.evmoto.common.security.context.SecurityContextHolder;
import com.evmoto.common.security.utils.ServletUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yelingjun
 * @title: HeaderInterceptor
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/21 20:11
 */
@Order(1)
public class HeaderInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        SecurityContextHolder.setRole(ServletUtils.getHeader(request, SecurityConstants.ROLE));
        SecurityContextHolder.setDeviceId(ServletUtils.getHeader(request, SecurityConstants.DEVICE_ID));
        SecurityContextHolder.setAuthorization(ServletUtils.getHeader(request, SecurityConstants.AUTHORIZATION));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityContextHolder.remove();
    }
}
