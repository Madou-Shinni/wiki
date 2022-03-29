package com.yum.wiki.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Yum
 * @version 1.0
 *
 * setnx工具
 */
@Component
public class RedisUtil {
    private static final Logger LOG = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private RedisTemplate redisTemplate;

    public Boolean validateRequest(String key, long second) {
        // 当key不存在时设置一个值
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, key, second, TimeUnit.SECONDS);
        if(flag)
            LOG.info("key不存在，放入：{}，过期 {} 秒",key,second);
        else
            LOG.info("key已存在：{}",key);
        return flag;
    }
}
