package com.hospital.seckill.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

@Configuration
@ConditionalOnClass(JedisCluster.class)
public class MeRedisConfig {

    //这里通过注解吧applicant.yml中的相关配置配置到jedis
    @Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;

    @Value("${spring.redis.cluster.max-redirects}")
    private int maxRetries;

    @Bean
    public JedisCluster jedisCluster(){
        //设置一个集合，放置集群的， 集群有ip和端口组成
        Set<HostAndPort> nodes=new HashSet<>();
        String[] hostandports=clusterNodes.split(",");
        for(String hostandport:hostandports){
            String[] hp=hostandport.split(":");
            nodes.add(new HostAndPort(hp[0],Integer.parseInt(hp[1])));

        }
        return new JedisCluster(nodes,maxRetries);

    }
}
