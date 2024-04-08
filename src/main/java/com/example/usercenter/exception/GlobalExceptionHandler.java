package com.example.usercenter.exception;

import com.example.usercenter.common.BaseResponse;
import com.example.usercenter.common.ErrorCode;
import com.example.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author hyh
 * @date 2024/4/4
 */
//RestControllerAdvice Spring AOP 在业务执行代码前和执行代码后（调用方法前后）都可以做一些额外的处理
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //这个方法只去捕获BusinessException异常
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error(e.getMessage() + "businessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }


    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeException(RuntimeException e) {
        log.error(e.getMessage() + "runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }



}
