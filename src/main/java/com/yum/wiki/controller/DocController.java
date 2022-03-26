package com.yum.wiki.controller;


import com.yum.wiki.request.DocQueryReq;
import com.yum.wiki.request.DocSaveReq;
import com.yum.wiki.request.DocUpdateReq;
import com.yum.wiki.result.DocQueryRes;
import com.yum.wiki.result.CommonResult;
import com.yum.wiki.result.PageRes;
import com.yum.wiki.result.tree.DocQueryResTree;
import com.yum.wiki.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Yum
 * @version 1.0
 */
@RestController
@RequestMapping("/doc")
public class DocController {


    @Autowired
    private DocService docService;

    /**
     * 一次性返回所有数据
     * @return
     */
    @GetMapping("/all")
    public CommonResult all() {
        List<DocQueryResTree> list = docService.all();
        CommonResult<List<DocQueryResTree>> result = new CommonResult<>();
        result.setData(list);
        return result;
    }

    /**
     * @Validated 校验规则
     * 对DocQueryReq参数做校验
     *
     * 查询分类列表
     * @param req
     * @return
     */
    @GetMapping("/list")
    public CommonResult list(@Validated DocQueryReq req) {
        PageRes<DocQueryRes> list = docService.list(req);
        CommonResult<PageRes<DocQueryRes>> result = new CommonResult<>();
        result.setData(list);
        return result;
    }


    /**
     * 修改分类
     * @param req
     * @return
     */
    @PutMapping("/update")
    public CommonResult update(@RequestBody DocUpdateReq req) {
        docService.update(req);
        CommonResult result = new CommonResult<>();
        return result;
    }

    /**
     * 新增分类
     * @param req
     * @return
     */
    @PostMapping("/save")
    public CommonResult save(@Validated @RequestBody DocSaveReq req) {
        docService.save(req);
        CommonResult result = new CommonResult<>();
        return result;
    }

    /**
     * 根据id删除分类
     * @param idsStr
     * @return
     */
    @DeleteMapping("/{idsStr}")
    public CommonResult delete(@PathVariable String idsStr) {
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);
        CommonResult result = new CommonResult<>();
        return result;
    }
}
