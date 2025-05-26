package com.hospital.seckill.service.impl;

import com.hospital.seckill.service.MyRedisService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MyRedisServiceImpl implements MyRedisService {

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private RedissonClient redissonClient;

    //在redis总设置库存
    @Override
    public void getRedisStock(List<Integer> good_id, List<Integer> good_count) {
        //秒杀时候有过期时间的
        for (int i = 0; i < good_id.size(); i++) {
            jedisCluster.setex("mygood_"+good_id.get(i), 7200, good_count.get(i)+"");
        }


    }

    @Override
    public boolean redisSeckill(int good_id) {
        boolean flag = false;
        //在redissionClient有一个getLock(),这里就是分布式锁的
        //拿到锁
        RLock lock = redissonClient.getLock("mygood_store_" + good_id);


        try {
            //一般的想法，秒杀哪一个商品
            //先取库存
            //设置锁的过期时间
            lock.lock(2 ,TimeUnit.SECONDS);
            //库存的秒杀发生成mycount>0
            if (jedisCluster.decr("mygoods" + good_id) >= 0) {
                //释放锁
                flag = true;
                System.out.println("Key type: " + jedisCluster.type("your_key"));
                System.out.println("Key value: " + jedisCluster.get("your_key"));
                System.out.println("抢到了-----");
            } else {
                jedisCluster.incr("mygoods" + good_id);
                flag = false;
                System.out.println("没抢到------");
            }
        } catch (Exception E) {
            E.printStackTrace();
        } finally {
            {
                lock.unlock();
            }
        }
        return flag;
    }


    @Override
    public boolean backSeckill(int good_id) {
        jedisCluster.incr("mygoods" + good_id);
        return true;

    }
}

