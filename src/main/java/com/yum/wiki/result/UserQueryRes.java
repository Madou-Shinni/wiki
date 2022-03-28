package com.yum.wiki.result;

import java.io.Serializable;

public class UserQueryRes implements Serializable {
    private Long id;

    private String loginName;

    private String name;

    private String password;

    private static final long serialVersionUID = 1L;

    public UserQueryRes(Long id, String loginName, String name, String password) {
        this.id = id;
        this.loginName = loginName;
        this.name = name;
        this.password = password;
    }

    public UserQueryRes() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }
}
