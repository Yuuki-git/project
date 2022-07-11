package com.mwz.miaoshaweb.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    /**
     * 删除缓存
     * @return
     */
    @Bean
    public Queue delCacheQueue(){return new Queue("delCache");}

    /**
     * 下单所用
     * @return
     */
    @Bean
    public Queue orderQueue(){return new Queue("orderQueue");}
}
