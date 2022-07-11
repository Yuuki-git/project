package com.mwz.miaoshaweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "com.mwz")
public class MiaoshaWebApplication {
    public static void main( String[] args ) {
        SpringApplication.run(MiaoshaWebApplication.class,args);
    }
}
