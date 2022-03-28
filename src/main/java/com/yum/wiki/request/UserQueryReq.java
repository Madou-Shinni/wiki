package com.yum.wiki.request;

import java.io.Serializable;

public class UserQueryReq extends PageReq implements Serializable {

    /**
     * 用户名
     */
    private String loginName;

    private static final long serialVersionUID = 1L;

    public UserQueryReq() {
    }

    public UserQueryReq(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
