package com.evmoto.common.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

public class RedissonConstants {

    public static final String REDISSON_PREFIX = "redisson";

    public static final String REDISSON_ENABLE = "redisson.enable";

    public static final String REDISSON_NAMESPACE = "redisson.namespace";
    /**
     * 分隔符
     */
    public final static String SEPARATOR = ":";
    /**
     * 命名空间
     */
    public static String NAMESPACE;

    @Value("${redisson.namespace:}")
    public void setNamespace(String namespace) {
        if (!StringUtils.hasLength(namespace)) {
            RedissonConstants.NAMESPACE = "";
            return;
        }
        if (namespace.endsWith(SEPARATOR)) {
            RedissonConstants.NAMESPACE = namespace;
            return;
        }
        RedissonConstants.NAMESPACE = namespace.concat(SEPARATOR);
    }
}
