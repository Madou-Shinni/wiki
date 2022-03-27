package com.yum.wiki.service.exception;

/**
 * @author Yum
 * @version 1.0
 *
 * 文档内容为空
 */
public class ContentNullException extends BaseException{
    public ContentNullException() {
        super();
    }

    public ContentNullException(String message) {
        super(message);
    }

    public ContentNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentNullException(Throwable cause) {
        super(cause);
    }

    protected ContentNullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
