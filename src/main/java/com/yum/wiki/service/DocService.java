package com.yum.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yum.wiki.config.RabbitmqConfig;
import com.yum.wiki.domain.Content;
import com.yum.wiki.domain.Doc;
import com.yum.wiki.domain.DocExample;
import com.yum.wiki.mapper.ContentMapper;
import com.yum.wiki.mapper.DocMapper;
import com.yum.wiki.mapper.DocMapperCust;
import com.yum.wiki.request.DocQueryReq;
import com.yum.wiki.request.DocSaveReq;
import com.yum.wiki.request.DocUpdateReq;
import com.yum.wiki.result.DocQueryRes;
import com.yum.wiki.result.PageRes;
import com.yum.wiki.result.tree.DocQueryResTree;
import com.yum.wiki.service.exception.ContentNullException;
import com.yum.wiki.service.exception.DocParentEqualsIdAndChildrenException;
import com.yum.wiki.service.exception.RepeatIncreaseVoteException;
import com.yum.wiki.utils.CopyUtil;
import com.yum.wiki.utils.RedisUtil;
import com.yum.wiki.utils.RequestContextUtil;
import com.yum.wiki.utils.SnowFlakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private DocMapperCust docMapperCust;
    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private SnowFlakeUtil snowFlakeUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WsService wsService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 数据量过大每次执行sql都会影响性能
     * 直接查询所有数据返回
     *
     * @return
     */
    public List<DocQueryResTree> all(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        // 按照sort字段升序
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        // 封装树型返回结果
        List<DocQueryResTree> result = CopyUtil.copyList(docList, DocQueryResTree.class);
        List<DocQueryResTree> finalResult = result.stream()
                // 过滤出一级文档
                .filter(item->item.getParent().equals(0L))
                .map(item->{
                    // 设置子级文档列表
                    item.setChildren(getChildrens(item,result));
                    return item;
                })
                .collect(Collectors.toList());
        return finalResult;
    }

    /**
     * 递归子级文档列表
     *
     * @param menu
     * @param list
     * @return
     */
    public List<DocQueryResTree> getChildrens(DocQueryResTree menu,List<DocQueryResTree> list) {
        List<DocQueryResTree> resTreeList = list.stream()
                // 过滤出当前文档的所有子级文档
                .filter(item->item.getParent().equals(menu.getId()))
                // 给当前文档设置子级文档(递归设置)
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
     * 修改文档
     */
    public void update(DocUpdateReq req) {
        // 不允许修改目标父节点为自身节点及其子孙节点（否则树型会断开消失）
        if(req.getParent().equals(req.getId())) {// 如果目标节点等于自身节点则修改失败
            throw new DocParentEqualsIdAndChildrenException("不允许修改目标节点为自身节点及其子孙节点");
        }
        // 根据id查询子孙节点
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andParentEqualTo(req.getId());
        List<Doc> docs = docMapper.selectByExample(docExample);
        if (!docs.isEmpty()) {// 如果存在子孙节点则比较目标节点是否是子孙节点
            docs.stream().forEach(item->{
                // 如果目标节点等于其子孙节点则修改失败
                if(req.getParent().equals(item.getId())) {
                    throw new DocParentEqualsIdAndChildrenException("不允许修改目标节点为自身节点及其子孙节点");
                }
            });
        }
        Doc doc = CopyUtil.copy(req,Doc.class);
        Content content = CopyUtil.copy(req,Content.class);
        docMapper.updateByPrimaryKey(doc);
        int rows = contentMapper.updateByPrimaryKeyWithBLOBs(content);
        if(rows == 0) {// 修改文档时可能内容表中没有该条数据，需要插入数据
            contentMapper.insert(content);
        }
    }

    /**
     * 新增文档
     */
    @Transactional
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req,Doc.class);
        Content content = CopyUtil.copy(req,Content.class);
        doc.setId(snowFlakeUtil.nextId());
        doc.setViewCount(0);
        doc.setViewCount(0);
        content.setId(doc.getId());
        docMapper.insert(doc);
        contentMapper.insert(content);
    }

    /**
     * 根据ID删除文档
     * @param id
     */
    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据ids批量删除删除文档
     * @param ids
     */
    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    /**
     * 根据id查询文档内容
     *
     * @return
     */
    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        // 文档阅读数+1
        docMapperCust.increaseViewCount(id);
        if(ObjectUtils.isEmpty(content)) {
            throw new ContentNullException("id【"+id+"】文档内容为空！");
        }
        return content.getContent();
    }

    /**
     * 点赞
     * @return
     */
    public void vite(Long id) {
        //docMapperCust.increaseVoteCount(id);
        // 远程ip+doc.id作为key,24小时内不能重复
        String key = RequestContextUtil.getRemoteAddr();
        if(redisUtil.validateRequest("DOC_VOTE" + id + "_" + key,5000))
            docMapperCust.increaseVoteCount(id);
        else
            throw new RepeatIncreaseVoteException("您已经点赞过了！");

        // 推送消息
        Doc docDb = docMapper.selectByPrimaryKey(id);
        String logId = MDC.get("LOG_ID");
        //wsService.sendInfo("【"+ docDb.getName() +"】被点赞！",logId);
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE,"VOTE.MESSAGE","【"+ docDb.getName() +"】被点赞！");
    }


    /**
     * 根据知识库分组统计文档(doc)数据，并更新到对应的知识库(ebook)中
     */
    public void updateEbookInfo() {
        docMapperCust.updateEbookInfo();
    }
}
