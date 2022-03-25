package com.yum.wiki.request;

/**
 * 请求参数类
 */
public class CategoryQueryReq extends PageReq {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryQueryReq{} " + super.toString();
    }
}
