package com.yum.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yum.wiki.domain.Ebook;
import com.yum.wiki.domain.EbookExample;
import com.yum.wiki.mapper.EbookMapper;
import com.yum.wiki.request.EbookReq;
import com.yum.wiki.result.EbookRes;
import com.yum.wiki.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yum
 * @version 1.0
 */
@Service
public class EbookService {
    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Autowired
    private EbookMapper ebookMapper;

    public List<EbookRes> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        // 如果有name（标题）参数传递进来就模糊查询
        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        // 分页(只对后面执行的第一条SQL有效)
        PageHelper.startPage(1,3);
        List<Ebook> ebooks = ebookMapper.selectByExample(ebookExample);
        // pagehelper还提供page对象
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebooks);
        //pageInfo.getTotal();
        //pageInfo.getPages();
        LOG.info("total：{}，pages：{}",pageInfo.getTotal(),pageInfo.getPages());
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
