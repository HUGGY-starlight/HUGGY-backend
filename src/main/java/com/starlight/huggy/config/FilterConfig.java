package com.starlight.huggy.config;

import com.starlight.huggy.filter.JwtTokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<JwtTokenFilter> getJwtTokenFilter() {
        FilterRegistrationBean<JwtTokenFilter> bean = new FilterRegistrationBean<>(new JwtTokenFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(0); // 낮은 번호의 필터가 제일 먼저 실행됨
        return bean;
    }

}
