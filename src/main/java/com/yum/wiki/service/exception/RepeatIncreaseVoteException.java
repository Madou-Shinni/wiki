package com.yum.wiki.service.exception;

/**
 * @author Yum
 * @version 1.0
 *
 * 重复点赞异常
 */
public class RepeatIncreaseVoteException extends BaseException{
    public RepeatIncreaseVoteException(String message) {
        super(message);
    }
}
