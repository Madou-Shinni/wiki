package com.yum.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yum.wiki.domain.Ebook;
import com.yum.wiki.domain.EbookExample;
import com.yum.wiki.mapper.EbookMapper;
import com.yum.wiki.request.EbookQueryReq;
import com.yum.wiki.request.EbookSaveReq;
import com.yum.wiki.request.EbookSearchReq;
import com.yum.wiki.request.EbookUpdateReq;
import com.yum.wiki.result.EbookQueryRes;
import com.yum.wiki.result.PageRes;
import com.yum.wiki.utils.CopyUtil;
import com.yum.wiki.utils.SnowFlakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
    @Autowired
    private SnowFlakeUtil snowFlakeUtil;

    public PageRes<EbookQueryRes> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        // 如果有name（标题）参数传递进来就模糊查询
        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        if(!ObjectUtils.isEmpty(req.getCategoryId2())) {
            criteria.andCategory2IdEqualTo(req.getCategoryId2());
        }
        // 分页(只对后面执行的第一条SQL有效)
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        // pagehelper还提供page对象
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        //pageInfo.getTotal();
        //pageInfo.getPages();
        /*ebooks.stream().forEach(item->{
            // BeanUtils.copyProperties(item,ebookRes);
            // 使用自己封装的CopyUtil 单体复制
            EbookQueryRes ebookRes = CopyUtil.copy(item, EbookQueryRes.class);
            resList.add(ebookRes);
        });*/
        // 使用自己封装的CopyUtil 列表复制
        List<EbookQueryRes> result = CopyUtil.copyList(ebookList, EbookQueryRes.class);
        PageRes<EbookQueryRes> pageRes = new PageRes<>();
        pageRes.setTotal(pageInfo.getTotal());
        pageRes.setList(result);
        return pageRes;
    }

    /**
     * 修改知识库
     */
    public void update(EbookUpdateReq req) {
        Ebook ebook = CopyUtil.copy(req,Ebook.class);
        ebookMapper.updateByPrimaryKey(ebook);
    }

    /**
     * 新增知识库
     */
    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req,Ebook.class);
        ebook.setId(snowFlakeUtil.nextId());
        ebook.setDocCount(0);
        ebook.setViewCount(0);
        ebook.setVoteCount(0);
        ebookMapper.insert(ebook);
    }

    /**
     * 根据ID删除知识库
     * @param id
     */
    public void delete(Long id) {
        ebookMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据标题、描述搜索知识库
     * @param req
     * @return
     */
    public PageRes<EbookQueryRes> search(EbookSearchReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList;
        // pagehelper分页对象
        PageInfo<Ebook> pageInfo = new PageInfo<>();
        // 如果没有传入值
        if(ObjectUtils.isEmpty(req.getText())) {
            ebookList = ebookMapper.selectByExample(null);
        }else {// 如果传入值
            // 如果有值就根据标题和描述搜索
            ebookList = ebookMapper.selectAllLikeNameOrDescription(req);
        }
        // 封装ebookList分页
        pageInfo.setList(ebookList);
        // 封装返回数据集
        List<EbookQueryRes> result = CopyUtil.copyList(ebookList, EbookQueryRes.class);
        PageRes<EbookQueryRes> pageRes = new PageRes<>();
        pageRes.setTotal(pageInfo.getTotal());
        pageRes.setList(result);
        return pageRes;
    }
}
