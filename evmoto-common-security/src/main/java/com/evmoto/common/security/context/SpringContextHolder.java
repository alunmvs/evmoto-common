package com.evmoto.common.security.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author yelingjun
 * @title: SpringContextHolder
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/28 21:33
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    // 静态保存上下文
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 静态方法：根据类型获取 Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(clazz);
    }

    /**
     * 校验上下文是否已加载
     */
    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("Spring 上下文未初始化，请检查是否被 Spring 管理");
        }
    }

}
