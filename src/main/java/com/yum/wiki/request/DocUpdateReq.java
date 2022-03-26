package com.yum.wiki.request;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class DocUpdateReq implements Serializable {
    private Long id;

    @NotNull(message = "【知识库】不能为空")
    private Long ebookId;

    @NotNull(message = "【父文档】不能为空")
    private Long parent;

    @NotNull(message = "【名称】不能为空")
    private String name;

    @NotNull(message = "【顺序】不能为空")
    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;

    private static final long serialVersionUID = 1L;

    public DocUpdateReq(Long id, Long ebookId, Long parent, String name, Integer sort, Integer viewCount, Integer voteCount) {
        this.id = id;
        this.ebookId = ebookId;
        this.parent = parent;
        this.name = name;
        this.sort = sort;
        this.viewCount = viewCount;
        this.voteCount = voteCount;
    }

    public DocUpdateReq() {
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
