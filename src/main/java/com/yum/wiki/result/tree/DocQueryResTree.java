package com.yum.wiki.result.tree;

import com.yum.wiki.result.DocQueryRes;

import java.util.List;

/**
 * @author Yum
 * @version 1.0
 *
 * 分类树型结构
 */
public class DocQueryResTree extends DocQueryRes {
    private List<DocQueryResTree> children;

    public List<DocQueryResTree> getChildren() {
        return children;
    }

    public void setChildren(List<DocQueryResTree> children) {
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
