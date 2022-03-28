package com.yum.wiki.request;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UserResetPasswordReq implements Serializable {
    private Long id;

    @NotNull(message = "【密码】不能为空")
    @Length(min = 6,max = 32,message = "【密码】6~20位")
    private String password;

    private static final long serialVersionUID = 1L;

    public UserResetPasswordReq() {
    }

    public UserResetPasswordReq(Long id, String password) {
        this.id = id;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
