package com.yum.wiki.result;

import java.io.Serializable;

public class CategoryQueryRes implements Serializable {
    private Long id;

    private Long parent;

    private String name;

    private Integer sort;

    private static final long serialVersionUID = 1L;

    public CategoryQueryRes(Long id, Long parent, String name, Integer sort) {
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.sort = sort;
    }

    public CategoryQueryRes() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
