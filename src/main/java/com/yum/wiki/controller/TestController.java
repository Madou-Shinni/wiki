package com.yum.wiki.controller;

import com.yum.wiki.domain.Test;
import com.yum.wiki.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yum
 * @version 1.0
 */
@RestController
public class TestController {
    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 优先读取配置项，配置项没有读取:后面的默认值
     */
    @Value("${test.hello:Test}")
    private String testHello;

    @Autowired
    private TestService testService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello world!" + testHello;
    }

    @PostMapping("/hello/post")
    public String helloPost(@RequestParam String name) {
        return "hello Post!" + name;
    }

    @GetMapping("/test/list")
    public List<Test> list() {
        return testService.list();
    }

    @GetMapping("/test/get/{token}")
    public Object get(@PathVariable Long token) {
        Object res = redisTemplate.opsForValue().get(token);
        LOG.info("key：{}，value：{}",token,res);
        return res;
    }
}
