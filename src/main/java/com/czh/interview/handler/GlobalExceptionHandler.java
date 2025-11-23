package com.czh.interview.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 捕获所有未处理的异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleAllException(Exception e) {
        log.error("全局异常捕获: ", e);
        return new ErrorResponse(500, "系统异常: " + e.getMessage());
    }

    // 捕获自定义异常
    @ExceptionHandler(BizException.class)
    @ResponseBody
    public ErrorResponse handleBizException(BizException e) {
        log.warn("业务异常: {}", e.getMessage());
        return new ErrorResponse(e.getCode(), e.getMessage());
    }
}
