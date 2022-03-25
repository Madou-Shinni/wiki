package com.yum.wiki.mapper;

import com.yum.wiki.domain.Ebook;
import com.yum.wiki.domain.EbookExample;
import com.yum.wiki.request.EbookSearchReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EbookMapper {
    long countByExample(EbookExample example);

    int deleteByExample(EbookExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Ebook record);

    int insertSelective(Ebook record);

    List<Ebook> selectByExample(EbookExample example);

    Ebook selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Ebook record, @Param("example") EbookExample example);

    int updateByExample(@Param("record") Ebook record, @Param("example") EbookExample example);

    int updateByPrimaryKeySelective(Ebook record);

    int updateByPrimaryKey(Ebook record);

    /**
     * 根据标题和描述搜索
     */
    List<Ebook> selectAllLikeNameOrDescription(EbookSearchReq req);
}
