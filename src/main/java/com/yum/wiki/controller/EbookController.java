package com.yum.wiki.controller;

import com.yum.wiki.request.EbookReq;
import com.yum.wiki.result.CommonResult;
import com.yum.wiki.result.EbookRes;
import com.yum.wiki.result.PageRes;
import com.yum.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yum
 * @version 1.0
 */
@RestController
@RequestMapping("/ebook")
public class EbookController {


    @Autowired
    private EbookService ebookService;


    @GetMapping("/list")
    public CommonResult list(EbookReq req) {
        PageRes<EbookRes> list = ebookService.list(req);
        CommonResult<PageRes<EbookRes>> result = new CommonResult<>();
        result.setData(list);
        return result;
    }
}
