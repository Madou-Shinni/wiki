package com.yum.wiki.result;

import java.util.List;

/**
 * @author Yum
 * @version 1.0
 *
 * 分页返回对象
 */
public class PageRes<T> {
    /**
     * 总条数
     */
    private long total;
    /**
     * 数据集
     */
    private List<T> list;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PageRes{");
        sb.append("total=").append(total);
        sb.append(", list=").append(list);
        sb.append('}');
        return sb.toString();
    }
}
