package com.hospital.seckill.service.impl;

import com.hospital.seckill.beans.MyOrder;
import com.hospital.seckill.mapper.MyOrderMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyRabbitMqListener {

    //注入MyOrderMapper
    @Autowired
    private MyOrderMapper myOrderMapper;
    //在rabbitmq中监听队列，队列名是reduceQueue
    @RabbitListener(queues = "reduceQueue")
    public void reduceStock(int id){
        //调用减库存的sql操作
        myOrderMapper.reduceStock(id);
    }
    //在rabbitmq中监听队列，队列名是orderQueue
    @RabbitListener(queues = "orderQueue")
    public void insertOrder(MyOrder myOrder){
        //调用加库存的sql操作
        myOrderMapper.insertOrder(myOrder);
    }
}
