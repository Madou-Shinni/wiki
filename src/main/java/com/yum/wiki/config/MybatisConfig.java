package com.yum.wiki.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yum
 * @version 1.0
 */
@Configuration
@MapperScan("com.yum.wiki.mapper")
public class MybatisConfig {
}
