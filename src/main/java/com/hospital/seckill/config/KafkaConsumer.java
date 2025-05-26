package com.hospital.seckill.config;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    //这里面调用发送信息的模版,通过调用这个模版,实例化就变成空消息
    private SimpMessagingTemplate simpMessagingTemplate;
    //作为kafka的参数
    public KafkaConsumer(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @KafkaListener(topics = "myhello",groupId = "test-consumer-group")
    public void listen(String message) {
        System.out.println("------------------------------------" );
        System.out.println(message);
        simpMessagingTemplate.convertAndSend("/topic/warn", message);
    }
}
