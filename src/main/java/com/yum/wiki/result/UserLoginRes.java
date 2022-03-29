package com.yum.wiki.result;

import java.io.Serializable;

public class UserLoginRes implements Serializable {
    private Long id;

    private String loginName;

    private String name;

    private String token;

    private static final long serialVersionUID = 1L;

    public UserLoginRes(Long id, String loginName, String name, String password) {
        this.id = id;
        this.loginName = loginName;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserLoginRes() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

}
