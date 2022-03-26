package com.yum.wiki.controller.exceptionhandle;

import com.yum.wiki.result.CommonResult;
import com.yum.wiki.service.exception.BaseException;
import com.yum.wiki.service.exception.DocParentEqualsIdAndChildrenException;
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
    @ExceptionHandler(value = {BindException.class, BaseException.class})
    public CommonResult validationExceptionHandler(Exception e) {
        CommonResult result = new CommonResult<>();
        result.setSuccess(false);
        if(e instanceof BindException) {
            LOG.warn("参数校验失败：{}", ((BindException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage());
            result.setMessage(((BindException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage());
        }else if(e instanceof DocParentEqualsIdAndChildrenException) {
            LOG.warn("收到异常攻击：文档修改接口受到攻击");
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
