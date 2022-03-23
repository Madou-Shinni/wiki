package com.yum.wiki.domain;

import java.io.Serializable;

public class Test implements Serializable {
    private Long id;

    private String name;

    private static final long serialVersionUID = 1L;

    public Test(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Test() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}