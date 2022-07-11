package com.mwz.miaoshaservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mwz.miaoshadao.mapper")
public class MiaoshaServiceApplication {
    public static void main( String[] args ) {
        SpringApplication.run(MiaoshaServiceApplication.class,args);
    }
}
