package com.evmoto.common.security.interceptor;

import com.evmoto.common.security.constants.SecurityConstants;
import com.evmoto.common.security.context.SecurityContextHolder;
import com.evmoto.common.security.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yelingjun
 * @title: HeaderInterceptor
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/21 20:11
 */
@Slf4j
public class HeaderInterceptor implements HandlerInterceptor {

    @Autowired
    private RedissonClient redissonClient;

    public HeaderInterceptor() {
        log.info("------------> HeaderInterceptor init");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        try {
            SecurityContextHolder.setRole(ServletUtils.getHeader(request, SecurityConstants.ROLE));
            SecurityContextHolder.setDeviceId(ServletUtils.getHeader(request, SecurityConstants.DEVICE_ID));
            SecurityContextHolder.setAuthorization(request.getHeader(SecurityConstants.AUTHORIZATION));
            String clientId = request.getHeader("clientId");//ServletUtils.getHeader(request, SecurityConstants.CLIENT_ID);
            if (StringUtils.isNotBlank(clientId)) {
                if ("driver".equals(SecurityContextHolder.getRole())) {
                    SecurityContextHolder.setDriverId(Integer.valueOf(clientId));
                } else if ("user".equals(SecurityContextHolder.getRole())) {
                    SecurityContextHolder.setUserId(Integer.valueOf(clientId));
                }
            }
        } catch (Exception e) {
            log.error("获取拦截器信息发生异常", e);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityContextHolder.remove();
    }
}
