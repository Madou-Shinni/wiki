package com.yum.wiki.mapper;

import org.apache.ibatis.annotations.Param;

public interface DocMapperCust {

    /**
     * 文档阅读数 +1
     * @param id
     */
    void increaseViewCount(@Param("id") Long id);

    /**
     * 点赞
     * @return
     */
    void increaseVoteCount(@Param("id") Long id);

    /**
     * 根据知识库分组统计文档(doc)数据，并更新到对应的知识库(ebook)中
     */
    void updateEbookInfo();
}
