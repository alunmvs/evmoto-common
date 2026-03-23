package com.evmoto.common.core.exception;

/**
 * @author yelingjun
 * @title: EvmotoException
 * @projectName evmoto-common
 * @description:
 * @date 2026/3/17 16:32
 */
public class EvmotoException extends RuntimeException {

    /**
     * 异常码
     */
    protected int code;

    private static final long serialVersionUID = 3160241586346324994L;

    public EvmotoException() {
    }

    public EvmotoException(Throwable cause) {
        super(cause);
    }

    public EvmotoException(String message) {
        super(message);
    }

    public EvmotoException(String message, Throwable cause) {
        super(message, cause);
    }

    public EvmotoException(int code, String message) {
        super(message);
        this.code = code;
    }

    public EvmotoException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
