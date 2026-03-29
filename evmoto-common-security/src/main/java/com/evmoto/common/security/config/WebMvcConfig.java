package com.evmoto.common.security.config;

import com.evmoto.common.security.interceptor.FeignRequestInterceptor;
import com.evmoto.common.security.interceptor.HeaderInterceptor;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yelingjun
 * @title: WebMvcConfig
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/23 20:54
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public WebMvcConfig() {
        log.info("------------> WebMvcConfig init");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    /**
     * 自定义请求头拦截器
     */
    @Bean
    public HeaderInterceptor headerInterceptor() {
        return new HeaderInterceptor();
    }

    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }
}
