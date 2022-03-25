package com.yum.wiki.request;

import org.apache.ibatis.type.Alias;

/**
 * @author Yum
 * @version 1.0
 */
@Alias(value = "ebooksearchreq")
public class EbookSearchReq extends PageReq{
    /**
     * 标题和描述
     */
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("EbookSearchReq{");
        sb.append("text='").append(text).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
