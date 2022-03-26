package com.yum.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yum.wiki.domain.Doc;
import com.yum.wiki.domain.DocExample;
import com.yum.wiki.mapper.DocMapper;
import com.yum.wiki.request.DocQueryReq;
import com.yum.wiki.request.DocSaveReq;
import com.yum.wiki.request.DocUpdateReq;
import com.yum.wiki.result.DocQueryRes;
import com.yum.wiki.result.PageRes;
import com.yum.wiki.result.tree.DocQueryResTree;
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
public class DocService {
    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Autowired
    private DocMapper docMapper;
    @Autowired
    private SnowFlakeUtil snowFlakeUtil;

    /**
     * 数据量过大每次执行sql都会影响性能
     * 直接查询所有数据返回
     *
     * @return
     */
    public List<DocQueryResTree> all() {
        DocExample docExample = new DocExample();
        // 按照sort字段升序
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        // 封装树型返回结果
        List<DocQueryResTree> result = CopyUtil.copyList(docList, DocQueryResTree.class);
        List<DocQueryResTree> finalResult = result.stream()
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
    public List<DocQueryResTree> getChildrens(DocQueryResTree menu,List<DocQueryResTree> list) {
        List<DocQueryResTree> resTreeList = list.stream()
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
    public PageRes<DocQueryRes> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        // 分页(只对后面执行的第一条SQL有效)
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);
        // pagehelper还提供page对象
        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        //pageInfo.getTotal();
        //pageInfo.getPages();
        /*docs.stream().forEach(item->{
            // BeanUtils.copyProperties(item,docRes);
            // 使用自己封装的CopyUtil 单体复制
            DocQueryRes docRes = CopyUtil.copy(item, DocQueryRes.class);
            resList.add(docRes);
        });*/
        // 使用自己封装的CopyUtil 列表复制
        List<DocQueryRes> result = CopyUtil.copyList(docList, DocQueryRes.class);
        PageRes<DocQueryRes> pageRes = new PageRes<>();
        pageRes.setTotal(pageInfo.getTotal());
        pageRes.setList(result);
        return pageRes;
    }

    /**
     * 修改分类
     */
    public void update(DocUpdateReq req) {
        Doc doc = CopyUtil.copy(req,Doc.class);
        docMapper.updateByPrimaryKey(doc);
    }

    /**
     * 新增分类
     */
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req,Doc.class);
        doc.setId(snowFlakeUtil.nextId());
        docMapper.insert(doc);
    }

    /**
     * 根据ID删除分类
     * @param id
     */
    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

}
