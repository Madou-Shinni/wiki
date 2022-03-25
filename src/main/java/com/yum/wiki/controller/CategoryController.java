package com.yum.wiki.controller;


import com.yum.wiki.request.CategoryQueryReq;
import com.yum.wiki.request.CategorySaveReq;
import com.yum.wiki.request.CategoryUpdateReq;
import com.yum.wiki.result.CategoryQueryRes;
import com.yum.wiki.result.CommonResult;
import com.yum.wiki.result.PageRes;
import com.yum.wiki.result.tree.CategoryQueryResTree;
import com.yum.wiki.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yum
 * @version 1.0
 */
@RestController
@RequestMapping("/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    /**
     * 一次性返回所有数据
     * @return
     */
    @GetMapping("/all")
    public CommonResult all() {
        List<CategoryQueryResTree> list = categoryService.all();
        CommonResult<List<CategoryQueryResTree>> result = new CommonResult<>();
        result.setData(list);
        return result;
    }

    /**
     * @Validated 校验规则
     * 对CategoryQueryReq参数做校验
     *
     * 查询分类列表
     * @param req
     * @return
     */
    @GetMapping("/list")
    public CommonResult list(@Validated CategoryQueryReq req) {
        PageRes<CategoryQueryRes> list = categoryService.list(req);
        CommonResult<PageRes<CategoryQueryRes>> result = new CommonResult<>();
        result.setData(list);
        return result;
    }


    /**
     * 修改分类
     * @param req
     * @return
     */
    @PutMapping("/update")
    public CommonResult update(@RequestBody CategoryUpdateReq req) {
        categoryService.update(req);
        CommonResult result = new CommonResult<>();
        return result;
    }

    /**
     * 新增分类
     * @param req
     * @return
     */
    @PostMapping("/save")
    public CommonResult save(@Validated @RequestBody CategorySaveReq req) {
        categoryService.save(req);
        CommonResult result = new CommonResult<>();
        return result;
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable Long id) {
        categoryService.delete(id);
        CommonResult result = new CommonResult<>();
        return result;
    }
}
