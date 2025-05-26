package com.hospital.seckill.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbirMQConfig {
    //第一个Queue
    @Bean
    public Queue reduceQueue(){
        return new Queue("reduceQueue");
    }
    @Bean
    public Queue orderQueue(){
        return new Queue("orderQueue");
    }
    //在产生一个exchange,减库存的exchange
    @Bean
    public FanoutExchange reduceExchange(){
        return new FanoutExchange("reduceExchange");
    }
    //在产生一个exchange,产生订单exchange
    @Bean
    public FanoutExchange orderExchange(){
        return new FanoutExchange("orderExchange");
    }
    //根据原理图，exchange和queue之间有一个binging，有一个绑定关系

    @Bean
    public Binding bindReduce(){
        return BindingBuilder.bind(reduceQueue()).to(reduceExchange());
    }

    //根据原理图，还需要 binding一个订单
    @Bean
    public Binding bindOrder(){
        return BindingBuilder.bind(orderQueue()).to(orderExchange());
    }
}
