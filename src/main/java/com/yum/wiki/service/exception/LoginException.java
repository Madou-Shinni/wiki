package com.yum.wiki.service.exception;

/**
 * @author Yum
 * @version 1.0
 *
 * 用户名或密码不正确
 */
public class LoginException extends BaseException{
    public LoginException(String message) {
        super(message);
    }
}
