package com.mwz.crm.config;

import com.mwz.crm.exceptions.NoLoginException;
import com.mwz.crm.interceptor.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author: yuuki
 * @Date: 2021-05-12 - 05 - 16:28
 * @Description: com.mwz.crm.config
 * @version: 1.0
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    /**
     * 实例化拦截器
     * @return
     */
    @Bean
    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }
    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(noLoginInterceptor())
                // 需要拦截的资源
                .addPathPatterns("/**")
                // 需要放行的资源
                .excludePathPatterns("/index","/user/login","/css/**","/images/**","/js/**","/lib/**");
    }
}
