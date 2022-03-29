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
    void increaseViteCount(@Param("id") Long id);
}
