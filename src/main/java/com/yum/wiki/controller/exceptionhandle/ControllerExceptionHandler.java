package com.yum.wiki.controller.exceptionhandle;

import com.yum.wiki.result.CommonResult;
import com.yum.wiki.service.exception.BaseException;
import com.yum.wiki.service.exception.ContentNullException;
import com.yum.wiki.service.exception.DocParentEqualsIdAndChildrenException;
import com.yum.wiki.service.exception.LoginNameEqualsException;
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
     * 参数校验异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {BindException.class})
    public CommonResult validationExceptionHandler(BindException e) {
        CommonResult result = new CommonResult<>();
        result.setSuccess(false);
        if(e instanceof BindException) {
            LOG.warn("参数校验失败：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            result.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        }
        return result;
    }

    /**
     * 自定义抛出异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {BaseException.class})
    public CommonResult ThrowException(BaseException e) {
        CommonResult result = new CommonResult<>();
        result.setSuccess(false);
        if(e instanceof DocParentEqualsIdAndChildrenException) {
            LOG.warn("收到异常攻击：文档修改接口受到攻击");
            result.setMessage(e.getMessage());
        }else if(e instanceof ContentNullException) {
            LOG.warn(e.getMessage());
            result.setMessage("文档内容为空");
        }else if(e instanceof LoginNameEqualsException) {
            LOG.warn(e.getMessage());
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 统一异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public CommonResult Exception(Exception e) {
        CommonResult result = new CommonResult<>();
        result.setSuccess(false);
        result.setMessage("系统出现未知异常，请联系管理员！");
        LOG.error("系统异常：{}",e);
        return result;
    }
}
