package com.yum.wiki.service;

import com.yum.wiki.domain.Ebook;
import com.yum.wiki.domain.EbookExample;
import com.yum.wiki.mapper.EbookMapper;
import com.yum.wiki.request.EbookReq;
import com.yum.wiki.result.EbookRes;
import com.yum.wiki.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yum
 * @version 1.0
 */
@Service
public class EbookService {
    @Autowired
    private EbookMapper ebookMapper;

    public List<EbookRes> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + req.getName() + "%");
        List<Ebook> ebooks = ebookMapper.selectByExample(ebookExample);
        // EbookRes返回对象，减少一些隐私信息传输
        List<EbookRes> resList = new ArrayList<>();
        /*ebooks.stream().forEach(item->{
            // BeanUtils.copyProperties(item,ebookRes);
            // 使用自己封装的CopyUtil 单体复制
            EbookRes ebookRes = CopyUtil.copy(item, EbookRes.class);
            resList.add(ebookRes);
        });*/

        // 使用自己封装的CopyUtil 列表复制
        List<EbookRes> result = CopyUtil.copyList(ebooks, EbookRes.class);
        return result;
    }
}
