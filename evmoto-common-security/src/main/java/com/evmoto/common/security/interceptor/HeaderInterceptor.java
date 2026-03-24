package com.evmoto.common.security.interceptor;

import com.evmoto.common.redis.client.EvmotoRedisClient;
import com.evmoto.common.security.constants.SecurityConstants;
import com.evmoto.common.security.context.SecurityContextHolder;
import com.evmoto.common.security.utils.ServletUtils;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
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
//@Component
public class HeaderInterceptor implements HandlerInterceptor {

    //@Autowired
    //private RedissonClient redissonClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("=== 拦截器生效了 ===");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        SecurityContextHolder.setRole(ServletUtils.getHeader(request, SecurityConstants.ROLE));
        SecurityContextHolder.setDeviceId(ServletUtils.getHeader(request, SecurityConstants.DEVICE_ID));
        String authorization = ServletUtils.getHeader(request, SecurityConstants.AUTHORIZATION);
        String clientId = ServletUtils.getHeader(request, SecurityConstants.CLIENT_ID);
        SecurityContextHolder.setAuthorization(authorization);
        //在这里要剥离出是乘客的id，还是司机的id
        String requestHeader = request.getHeader("Authorization");
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            requestHeader = requestHeader.substring(requestHeader.indexOf(" ") + 1);
            String key = null;
            int length = requestHeader.length();
            if(length > 32){
                key = requestHeader.substring(length - 32);
            }else{
                key = requestHeader;
            }
            //RBucket<String> bucket = redissonClient.getBucket(key);
            String id = "123";//bucket.get();
            if (StringUtils.isNotBlank(id)) {
                if ("driver".equals(SecurityContextHolder.getRole())) {
                    SecurityContextHolder.setDriverId(Integer.valueOf(id));
                } else if ("user".equals(SecurityContextHolder.getRole())) {
                    SecurityContextHolder.setUserId(Integer.valueOf(id));
                }
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityContextHolder.remove();
    }
}
