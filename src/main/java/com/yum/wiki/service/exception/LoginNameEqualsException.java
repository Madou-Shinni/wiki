package com.yum.wiki.service.exception;

/**
 * @author Yum
 * @version 1.0
 */
public class LoginNameEqualsException extends BaseException{
    public LoginNameEqualsException() {
        super();
    }

    public LoginNameEqualsException(String message) {
        super(message);
    }

    public LoginNameEqualsException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginNameEqualsException(Throwable cause) {
        super(cause);
    }

    protected LoginNameEqualsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
