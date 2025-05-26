package com.hospital.seckill.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisCluster;

import java.util.Arrays;

@Configuration
@ConditionalOnClass(JedisCluster.class)
public class RedisConfig {

    //这里通过注解吧applicant.yml中的相关配置配置到jedis
    @Value("${spring.redis.cluster.nodes}")
    private String nodes;

    @Value("${spring.redis.cluster.max-redirects}")
    private int maxRetries;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        RedisClusterConfiguration configuration=new RedisClusterConfiguration(Arrays.asList(nodes.split(",")));
        configuration.setMaxRedirects(maxRetries);
        return  new LettuceConnectionFactory(configuration);
    }
    @Bean
    public RedisTemplate<String,String> redisTemplate(){
        RedisTemplate<String,String> template=new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return  template;
    }

}
