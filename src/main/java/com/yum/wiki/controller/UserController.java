package com.yum.wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.yum.wiki.request.*;
import com.yum.wiki.result.CommonResult;
import com.yum.wiki.result.PageRes;
import com.yum.wiki.result.UserLoginRes;
import com.yum.wiki.result.UserQueryRes;
import com.yum.wiki.service.UserService;
import com.yum.wiki.utils.SnowFlakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author Yum
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SnowFlakeUtil snowFlakeUtil;


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

    /**
     * 登录
     * @param req
     * @return
     */
    @PostMapping("/login")
    public CommonResult login(@Validated @RequestBody UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));// 加密
        UserLoginRes res = userService.login(req);

        Long token = snowFlakeUtil.nextId();
        LOG.info("生成单点登录token：{}，放入redis中",token);
        res.setToken(token.toString());
        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(res),3600 * 24, TimeUnit.SECONDS);

        CommonResult result = new CommonResult<>();
        result.setData(res);
        return result;
    }

}
