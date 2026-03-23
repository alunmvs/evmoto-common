package com.evmoto.common.security.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.evmoto.common.security.constants.SecurityConstants;
import com.evmoto.common.security.utils.ConvertUtls;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取当前线程变量中的 用户id、用户名称、Token等信息 
 * 注意： 必须在网关通过请求头的方法传入，同时在HeaderInterceptor拦截器设置值。 否则这里无法获取
 *
 * @author ruoyi
 */
public class SecurityContextHolder {

    private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? StringUtils.EMPTY : value);
    }

    public static String get(String key) {
        Map<String, Object> map = getLocalMap();
        return ConvertUtls.toStr(map.getOrDefault(key, StringUtils.EMPTY));
    }

    public static <T> T get(String key, Class<T> clazz) {
        Map<String, Object> map = getLocalMap();
        return cast(map.getOrDefault(key, null));
    }

    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<String, Object>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setRole(String role) {
        set(SecurityConstants.ROLE, role);
    }

    public static String getRole() {
        return get(SecurityConstants.ROLE);
    }

    public static void setDeviceId(String deviceId) {
        set(SecurityConstants.DEVICE_ID, deviceId);
    }

    public static String getDeviceId() {
        return get(SecurityConstants.DEVICE_ID);
    }

    public static void setAuthorization(String authorization) {
        set(SecurityConstants.AUTHORIZATION, authorization);
    }

    public static String getAuthorization() {
        return get(SecurityConstants.AUTHORIZATION);
    }

    public static void remove()
    {
        THREAD_LOCAL.remove();
    }

    private static <T> T cast(Object obj) {
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }
}
