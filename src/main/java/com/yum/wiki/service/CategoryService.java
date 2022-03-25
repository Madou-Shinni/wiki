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
import com.yum.wiki.result.tree.CategoryQueryResTree;
import com.yum.wiki.utils.CopyUtil;
import com.yum.wiki.utils.SnowFlakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 数据量过大每次执行sql都会影响性能
     * 直接查询所有数据返回
     *
     * @return
     */
    public List<CategoryQueryResTree> all() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        // 封装树型返回结果
        List<CategoryQueryResTree> result = CopyUtil.copyList(categoryList, CategoryQueryResTree.class);
        List<CategoryQueryResTree> finalResult = result.stream()
                // 过滤出一级分类
                .filter(item->item.getParent().equals(0L))
                .map(item->{
                    // 设置子级分类列表
                    item.setChildren(getChildrens(item,result));
                    return item;
                })
                .collect(Collectors.toList());
        return finalResult;
    }

    /**
     * 递归子级分类列表
     *
     * @param menu
     * @param list
     * @return
     */
    public List<CategoryQueryResTree> getChildrens(CategoryQueryResTree menu,List<CategoryQueryResTree> list) {
        List<CategoryQueryResTree> resTreeList = list.stream()
                // 过滤出当前分类的所有子级分类
                .filter(item->item.getParent().equals(menu.getId()))
                // 给当前分类设置子级分类(递归设置)
                .map(item->{
                    item.setChildren(getChildrens(item,list));
                    return item;
                })
                .collect(Collectors.toList());
        return resTreeList;
    }


    /**
     * 分页数据
     * @param req
     * @return
     */
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
     * 修改分类
     */
    public void update(CategoryUpdateReq req) {
        Category category = CopyUtil.copy(req,Category.class);
        categoryMapper.updateByPrimaryKey(category);
    }

    /**
     * 新增分类
     */
    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req,Category.class);
        category.setId(snowFlakeUtil.nextId());
        categoryMapper.insert(category);
    }

    /**
     * 根据ID删除分类
     * @param id
     */
    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

}
