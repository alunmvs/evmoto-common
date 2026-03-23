package com.evmoto.common.feign.utils;

import com.evmoto.common.core.constant.Constants;
import com.evmoto.common.core.domain.R;
import com.evmoto.common.core.exception.EvmotoException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Supplier;

/**
 *
 */
@Slf4j
public class ApiResultUtils {

    private ApiResultUtils() {}

    /**
     * executeFacade
     * @date 2024/11/21 14:05
     * @param supplier supplier
     * @return T
     */
    public static <T> T executeFacade(Supplier<R<T>> supplier){
        return executeFacade(supplier, true);
    }

    public static <T> T executeFacade(Supplier<R<T>> supplier, boolean throwException){
        try {
            R<T> apiResult = supplier.get();
            if (apiResult.getCode() == Constants.SUCCESS) {
                return apiResult.getData();
            }
            if (throwException) {
                throw new EvmotoException(apiResult.getCode(), apiResult.getMsg());
            } else {
                return null;
            }
        } catch (Exception exception) {
            if (exception instanceof EvmotoException) {
                throw exception;
            } else {
                log.error("feign error", exception);
                throw new EvmotoException("feign 调用失败");
            }
        }
    }

    public static <T> Optional<T> executeFacadeOpt(Supplier<R<T>> supplier){
        try {
            R<T> apiResult = supplier.get();
            if (apiResult.getCode() == Constants.SUCCESS) {
                return Optional.ofNullable(apiResult.getData());
            }
            throw new EvmotoException(apiResult.getCode(), apiResult.getMsg());
        } catch (Exception exception) {
            if (exception instanceof EvmotoException) {
                throw exception;
            } else {
                log.error("feign error", exception);
                throw new EvmotoException("feign 调用失败");
            }
        }
    }
}