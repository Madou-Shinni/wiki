package com.yum.wiki.service.exception;

/**
 * @author Yum
 * @version 1.0
 *
 * 修改文档时目标节点等于自身节点及其子孙节点异常
 */
public class DocParentEqualsIdAndChildrenException extends BaseException{
    public DocParentEqualsIdAndChildrenException() {
        super();
    }

    public DocParentEqualsIdAndChildrenException(String message) {
        super(message);
    }

    public DocParentEqualsIdAndChildrenException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocParentEqualsIdAndChildrenException(Throwable cause) {
        super(cause);
    }

    protected DocParentEqualsIdAndChildrenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
