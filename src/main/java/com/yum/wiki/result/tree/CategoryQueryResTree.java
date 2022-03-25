package com.yum.wiki.result.tree;

import com.yum.wiki.result.CategoryQueryRes;

import java.util.List;

/**
 * @author Yum
 * @version 1.0
 *
 * 分类树型结构
 */
public class CategoryQueryResTree extends CategoryQueryRes {
    private List<CategoryQueryResTree> children;

    public List<CategoryQueryResTree> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryQueryResTree> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CategoryQueryResTree{");
        sb.append("children=").append(children);
        sb.append('}');
        return sb.toString();
    }
}
