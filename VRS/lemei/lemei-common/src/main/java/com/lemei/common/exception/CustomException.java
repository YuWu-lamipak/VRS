package com.lemei.common.exception;


/**
 * 自定义 异常抛出
 */
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 重载构造函数
     * @param message
     * @param cause
     */
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 重载构造函数
     * @param message
     */
    public CustomException(String message) {
        super(message);
    }
}
