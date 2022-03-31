package com.yum.wiki.config;

import com.yum.wiki.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Yum
 * @version 1.0
 *
 */
@Configuration
public class SpringMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;
    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")// 针对所有请求
                .excludePathPatterns(
                        "/user/login",// 排除 login 接口
                        "/category/all",
                        "/ebook/list",
                        "/doc/all/**",
                        "/doc/findContent/**",
                        "/doc/vote/**",
                        "/ebookSnapshot/getStatistic",
                        "/ebookSnapshot/get30Statistic"
                );
    }
}
