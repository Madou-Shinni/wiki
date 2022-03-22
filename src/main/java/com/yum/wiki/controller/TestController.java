package com.yum.wiki.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yum
 * @version 1.0
 */
@RestController
public class TestController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello world!";
    }
}
