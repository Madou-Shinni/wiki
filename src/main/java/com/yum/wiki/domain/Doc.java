package com.yum.wiki.domain;

import java.io.Serializable;

public class Doc implements Serializable {
    private Long id;

    private Long ebookId;

    private Long parent;

    private String name;

    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;

    private static final long serialVersionUID = 1L;

    public Doc(Long id, Long ebookId, Long parent, String name, Integer sort, Integer viewCount, Integer voteCount) {
        this.id = id;
        this.ebookId = ebookId;
        this.parent = parent;
        this.name = name;
        this.sort = sort;
        this.viewCount = viewCount;
        this.voteCount = voteCount;
    }

    public Doc() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
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

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }
}