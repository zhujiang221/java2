package com.hospital.seckill.service.impl;


import com.hospital.seckill.beans.MyOrder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyRabbitPublisher {
    //SpringBoot仲有一个模版
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void payment(MyOrder myOrder){
        //发送消息，第一个参数是交换机的名字，第二个参数是路由的键，第三个参数是消息的内容
        rabbitTemplate.convertAndSend("reduceExchange","",myOrder.getGood_id());
        //发送订单消息
        rabbitTemplate.convertAndSend("orderExchange","",myOrder);

    }

}
