package com.yum.wiki.request;

import java.io.Serializable;

/**
 * 请求参数类
 */
public class EbookReq implements Serializable {
    private Long id;

    private String name;

    private static final long serialVersionUID = 1L;

    public EbookReq(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public EbookReq() {
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
