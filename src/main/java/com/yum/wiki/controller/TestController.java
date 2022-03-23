package com.yum.wiki.controller;

import com.yum.wiki.TestService.TestService;
import com.yum.wiki.domain.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Yum
 * @version 1.0
 */
@RestController
public class TestController {

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
}
