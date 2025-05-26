package com.hospital.seckill.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class RedissonConfig {
    @Value("${spring.redisson.nodes}")
    public String nodes;
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(){

        Config config=new Config();
        config.useClusterServers().setNodeAddresses(Arrays.asList(nodes.split(",")));
        return Redisson.create(config);
    }
}
