package com.yum.wiki.controller;

import com.yum.wiki.domain.Ebook;
import com.yum.wiki.result.CommonResult;
import com.yum.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public CommonResult list() {
        List<Ebook> list = ebookService.list();
        CommonResult<List<Ebook>> result = new CommonResult<>();
        result.setData(list);
        return result;
    }
}
