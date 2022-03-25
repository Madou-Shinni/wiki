package com.yum.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yum.wiki.domain.Category;
import com.yum.wiki.domain.CategoryExample;
import com.yum.wiki.mapper.CategoryMapper;
import com.yum.wiki.request.CategoryQueryReq;
import com.yum.wiki.request.CategorySaveReq;
import com.yum.wiki.request.CategoryUpdateReq;
import com.yum.wiki.result.CategoryQueryRes;
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
public class CategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SnowFlakeUtil snowFlakeUtil;

    public PageRes<CategoryQueryRes> list(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%"+req.getName()+"%");
        }
        // 分页(只对后面执行的第一条SQL有效)
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        // pagehelper还提供page对象
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        //pageInfo.getTotal();
        //pageInfo.getPages();
        /*categorys.stream().forEach(item->{
            // BeanUtils.copyProperties(item,categoryRes);
            // 使用自己封装的CopyUtil 单体复制
            CategoryQueryRes categoryRes = CopyUtil.copy(item, CategoryQueryRes.class);
            resList.add(categoryRes);
        });*/
        // 使用自己封装的CopyUtil 列表复制
        List<CategoryQueryRes> result = CopyUtil.copyList(categoryList, CategoryQueryRes.class);
        PageRes<CategoryQueryRes> pageRes = new PageRes<>();
        pageRes.setTotal(pageInfo.getTotal());
        pageRes.setList(result);
        return pageRes;
    }

    /**
     * 修改知识库
     */
    public void update(CategoryUpdateReq req) {
        Category category = CopyUtil.copy(req,Category.class);
        categoryMapper.updateByPrimaryKey(category);
    }

    /**
     * 新增知识库
     */
    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req,Category.class);
        category.setId(snowFlakeUtil.nextId());
        categoryMapper.insert(category);
    }

    /**
     * 根据ID删除知识库
     * @param id
     */
    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

}
