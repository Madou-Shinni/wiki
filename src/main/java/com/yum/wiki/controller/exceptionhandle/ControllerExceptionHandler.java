package com.yum.wiki.controller.exceptionhandle;

import com.yum.wiki.result.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Yum
 * @version 1.0
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 统一异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {BindException.class})
    public CommonResult validationExceptionHandler(BindException e) {
        CommonResult result = new CommonResult<>();
        LOG.warn("参数校验失败：{}",e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        result.setSuccess(false);
        result.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return result;
    }
}
