package com.yum.wiki.domain;

import java.io.Serializable;

public class Content implements Serializable {
    private Long id;

    private String content;

    private static final long serialVersionUID = 1L;

    public Content(Long id) {
        this.id = id;
    }

    public Content(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public Content() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}