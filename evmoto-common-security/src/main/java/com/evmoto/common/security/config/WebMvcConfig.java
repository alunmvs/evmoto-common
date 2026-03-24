package com.evmoto.common.security.config;

import com.evmoto.common.security.interceptor.HeaderInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yelingjun
 * @title: WebMvcConfig
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/23 20:54
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 1. 先注入拦截器（必须）
    //@Autowired
    //private HeaderInterceptor headerInterceptor;

    public WebMvcConfig() {
        System.out.println("初始化拦截器了");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerInterceptor()).addPathPatterns("/**")
                //.excludePathPatterns(excludeUrls).order(-10);
        ;
    }
    /**
     * 自定义请求头拦截器
     */
    @Bean
    public HeaderInterceptor headerInterceptor() {
        return new HeaderInterceptor();
    }
}
