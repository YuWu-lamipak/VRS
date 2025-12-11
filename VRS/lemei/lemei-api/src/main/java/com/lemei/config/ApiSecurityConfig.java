package com.lemei.config;

import com.lemei.filter.ApiUserContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

/**
 * API模块轻量级安全配置
 * 用于解决多用户会话隔离问题
 */
@Configuration
public class ApiSecurityConfig {

    @Bean
    public FilterRegistrationBean<ApiUserContextFilter> apiUserContextFilter() {
        FilterRegistrationBean<ApiUserContextFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApiUserContextFilter());
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}