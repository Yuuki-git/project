package com.mwz.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.SpringServletContainerInitializer;

/**
 * @author: yuuki
 * @Date: 2021-04-05 - 04 - 15:59
 * @Description: com.mwz.crm
 * @version: 1.0
 */
@SpringBootApplication
@MapperScan("com.mwz.crm.dao")
public class Stater extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Stater.class);
    }
    //设置web项目的启动入口 重写config的方法

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Stater.class);
    }
}
