package com.example.usercenter.exception;

import com.example.usercenter.common.ErrorCode;

/**
 * 给原本的异常类RuntimeException扩充了两个字段code、description
 * 优点：1、相比Java内置的异常类，支持更多的属性
 *      2、自定义构造函数，更灵活、快捷地给属性赋值
 * @author hyh
 * @date 2024/4/4
 */

public class BusinessException extends RuntimeException {
    private final int code;
    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
