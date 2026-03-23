package com.evmoto.common.security.exception;

import com.evmoto.common.core.domain.R;
import com.evmoto.common.core.exception.EvmotoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yelingjun
 * @title: GlobalExceptionHandler
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/17 17:09
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 权限码异常
     */
    @ExceptionHandler(EvmotoException.class)
    public R handleNotPermissionException(EvmotoException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',错误信息 {}", requestURI,e.getMessage());
        return R.fail(e.getCode(), e.getMessage());
    }
}
