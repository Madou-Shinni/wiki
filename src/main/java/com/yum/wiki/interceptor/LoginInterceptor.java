package com.yum.wiki.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Yum
 * @version 1.0
 *
 * 登录拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (HttpMethod.OPTIONS.toString().equals(request.getMethod()))// 解决后端获取不到前端自定义token值
            return true;
        // 获取请求头中的token
        String token = request.getHeader("token");
        // 打印请求信息
        LOG.info("登录校验开始，token：{}",token);
        if(token == null || token.isEmpty()) {
            LOG.info("token为空，请求被拦截");
            // 401 没有权限
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        Object o = redisTemplate.opsForValue().get(token);
        if(o == null) {
            LOG.warn("token无效，请求被拦截");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        LOG.info("已登录：{}",o);
        return true;
    }
}
