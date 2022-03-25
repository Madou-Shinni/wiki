package com.yum.wiki.request;


import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * @author Yum
 * @version 1.0
 *
 * 分页参数
 */
public class PageReq {
    /**
     * @NotNull ：
     * validation 参数不能为空注解
     * 页码
     */
    @NotNull(message = "【页码】不能为空")
    private int page;
    /**
     * 每页条数
     */
    @NotNull(message = "【每页条数】不能为空")
    @Max(value = 1000,message = "【每页条数】不能超过1000")
    private int size;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PageReq{");
        sb.append("page=").append(page);
        sb.append(", size=").append(size);
        sb.append('}');
        return sb.toString();
    }

}
