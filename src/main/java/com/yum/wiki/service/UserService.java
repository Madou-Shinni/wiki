package com.yum.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yum.wiki.domain.User;
import com.yum.wiki.domain.UserExample;
import com.yum.wiki.mapper.UserMapper;
import com.yum.wiki.request.*;
import com.yum.wiki.result.PageRes;
import com.yum.wiki.result.UserLoginRes;
import com.yum.wiki.result.UserQueryRes;
import com.yum.wiki.service.exception.LoginException;
import com.yum.wiki.service.exception.LoginNameEqualsException;
import com.yum.wiki.utils.CopyUtil;
import com.yum.wiki.utils.SnowFlakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author Yum
 * @version 1.0
 */
@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SnowFlakeUtil snowFlakeUtil;

    public PageRes<UserQueryRes> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameLike("%" + req.getLoginName() + "%");
        }
        // 分页(只对后面执行的第一条SQL有效)
        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);
        // pagehelper还提供page对象
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        //pageInfo.getTotal();
        //pageInfo.getPages();
        /*users.stream().forEach(item->{
            // BeanUtils.copyProperties(item,userRes);
            // 使用自己封装的CopyUtil 单体复制
            UserQueryRes userRes = CopyUtil.copy(item, UserQueryRes.class);
            resList.add(userRes);
        });*/
        // 使用自己封装的CopyUtil 列表复制
        List<UserQueryRes> result = CopyUtil.copyList(userList, UserQueryRes.class);
        PageRes<UserQueryRes> pageRes = new PageRes<>();
        pageRes.setTotal(pageInfo.getTotal());
        pageRes.setList(result);
        return pageRes;
    }

    /**
     * 修改用户
     */
    public void update(UserUpdateReq req) {
        if (!ObjectUtils.isEmpty(req.getId())) {
            User user = CopyUtil.copy(req,User.class);
            user.setLoginName(null);
            user.setPassword(null);
            // updateByPrimaryKeySelective如果有字段是空就不会更新空的字段
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    /**
     * 修改用户
     */
    public void resetPassword(UserResetPasswordReq req) {
        if (!ObjectUtils.isEmpty(req.getId())) {
            User user = CopyUtil.copy(req,User.class);
            user.setLoginName(null);
            // updateByPrimaryKeySelective如果有字段是空就不会更新空的字段
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    /**
     * 新增用户
     */
    public void save(UserSaveReq req) {
        // 判断用户名是否已存在
        if(!ObjectUtils.isEmpty(SelectByLoginName(req.getLoginName()))) {
            // 已存在则不允许添加用户
            throw new LoginNameEqualsException("用户名已存在！");
        };
        User user = CopyUtil.copy(req,User.class);
        user.setId(snowFlakeUtil.nextId());
        userMapper.insert(user);
    }

    /**
     * 根据ID删除用户
     * @param id
     */
    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }


    /**
     * 根据用户名查询用户
     *
     * @param loginName
     * @return
     */
    public User SelectByLoginName(String loginName) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if(userList.isEmpty()) {
            return null;
        }
        return userList.get(0);
    }

    /**
     *  登录（防止被攻击应该统一返回用户名或密码不正确而不是具体信息）
     * （日志打印应该更详细：使我们能更容易的分析问题）
     *  -- 1.频繁出现密码错误可能是有人在破解密码，需要锁定账户
     * @param req
     * @return
     */
    public UserLoginRes login(UserLoginReq req) {
        User user = SelectByLoginName(req.getLoginName());
        if(ObjectUtils.isEmpty(user)) {
            // 用户不存在!
            LOG.info("用户名不存在：{}",req.getLoginName());
            throw new LoginException("用户名或密码不正确！");
        }else if(!user.getPassword().equals(req.getPassword())) {// 比对查出来的密码和前端传过来加密之后的密码
            // 密码不正确
            LOG.info("输入密码错误，输入的密码：{},数据库的密码：{}",req.getLoginName(),user.getPassword());
            throw new LoginException("用户名或密码不正确！");
        }
        UserLoginRes res = CopyUtil.copy(user, UserLoginRes.class);
        return res;
    }
}
