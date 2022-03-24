package com.yum.wiki.controller;

import com.yum.wiki.request.EbookQueryReq;
import com.yum.wiki.request.EbookSaveReq;
import com.yum.wiki.request.EbookUpdateReq;
import com.yum.wiki.result.CommonResult;
import com.yum.wiki.result.EbookQueryRes;
import com.yum.wiki.result.PageRes;
import com.yum.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yum
 * @version 1.0
 */
@RestController
@RequestMapping("/ebook")
public class EbookController {


    @Autowired
    private EbookService ebookService;


    /**
     * 查询知识库列表
     * @param req
     * @return
     */
    @GetMapping("/list")
    public CommonResult list(EbookQueryReq req) {
        PageRes<EbookQueryRes> list = ebookService.list(req);
        CommonResult<PageRes<EbookQueryRes>> result = new CommonResult<>();
        result.setData(list);
        return result;
    }


    /**
     * 修改知识库
     * @param req
     * @return
     */
    @PutMapping("/update")
    public CommonResult update(@RequestBody EbookUpdateReq req) {
        ebookService.update(req);
        CommonResult result = new CommonResult<>();
        return result;
    }

    /**
     * 新增知识库
     * @param req
     * @return
     */
    @PostMapping("/save")
    public CommonResult save(@RequestBody EbookSaveReq req) {
        ebookService.save(req);
        CommonResult result = new CommonResult<>();
        return result;
    }
}
