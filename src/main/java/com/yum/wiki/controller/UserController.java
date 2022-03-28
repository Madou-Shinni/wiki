package com.yum.wiki.controller;

import com.yum.wiki.request.UserQueryReq;
import com.yum.wiki.request.UserResetPasswordReq;
import com.yum.wiki.request.UserSaveReq;
import com.yum.wiki.request.UserUpdateReq;
import com.yum.wiki.result.CommonResult;
import com.yum.wiki.result.PageRes;
import com.yum.wiki.result.UserQueryRes;
import com.yum.wiki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yum
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;


    /**
     * @Validated 校验规则
     * 对UserQueryReq参数做校验
     *
     * 查询用户列表
     * @param req
     * @return
     */
    @GetMapping("/list")
    public CommonResult list(@Validated UserQueryReq req) {
        PageRes<UserQueryRes> list = userService.list(req);
        CommonResult<PageRes<UserQueryRes>> result = new CommonResult<>();
        result.setData(list);
        return result;
    }


    /**
     * 修改用户
     * @param req
     * @return
     */
    @PutMapping("/update")
    public CommonResult update(@Validated @RequestBody UserUpdateReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));// 加密
        userService.update(req);
        CommonResult result = new CommonResult<>();
        return result;
    }

    /**
     * 重置密码
     * @param req
     * @return
     */
    @PutMapping("/resetPassword")
    public CommonResult resetPassword(@Validated @RequestBody UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));// 加密
        userService.resetPassword(req);
        CommonResult result = new CommonResult<>();
        return result;
    }

    /**
     * 新增用户
     * @param req
     * @return
     */
    @PostMapping("/save")
    public CommonResult save(@Validated @RequestBody UserSaveReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));// 加密
        userService.save(req);
        CommonResult result = new CommonResult<>();
        return result;
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable Long id) {
        userService.delete(id);
        CommonResult result = new CommonResult<>();
        return result;
    }

}
