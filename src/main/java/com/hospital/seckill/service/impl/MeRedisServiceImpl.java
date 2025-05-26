package com.hospital.seckill.service.impl;

import com.hospital.seckill.service.MeRedisService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MeRedisServiceImpl implements MeRedisService {
    @Autowired
    private RedisTemplate redisTemplate;
    //在redis中设置库存
    @Autowired
    private RedissonClient redissonClient;


    @Override
    public void getRedisStock(List<Integer> good_id ,List<Integer> good_count) {
        //设置秒杀的键是有过期时间的,如果秒杀2个小时的话，应该2*60*60=7200
        //简直商品id，这里给商品id+good
        //这里good_id和good_count是对应关系，这里循环索引变量
        //先获取
        ValueOperations<String,String> opt=redisTemplate.opsForValue();

        for (int i = 0; i < good_id.size(); i++) {
            opt.set("goodkey_"+good_id.get(i),good_count.get(i)+"",Duration.ofHours(2));
        }

    }


    @Override
    public boolean redisSeckill(int good_id,int user_id) {
        boolean flag = false;
        //在redissionClient有一个getLock(),这里就是分布式锁的
        //拿到锁
        RLock lock = redissonClient.getLock("mygood_store_" + good_id);

        ValueOperations<String,String> opt=redisTemplate.opsForValue();
        try {
            //一般的想法，秒杀哪一个商品
            //先取库存
            //设置锁的过期时间
            lock.lock(2 ,TimeUnit.SECONDS);
            //库存的秒杀发生成mycount>0
            String myvalue=opt.get("goodkey_"+good_id);
            if(myvalue!=null && myvalue.equals("1")){
                return false;
            }
            if (opt.decrement("goodkey_"+good_id) >= 0) {
                //释放锁
                opt.set(good_id+"_success_"+user_id,"1",Duration.ofMinutes(20));
                flag = true;
                System.out.println("抢到l");
            } else {
                opt.increment("goodkey_"+good_id);
                flag = false;
                System.out.println("没抢到");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            {
                lock.unlock();
            }
        }
        return flag;
    }

    @Override
    public boolean backSeckill(int good_id) {
        ValueOperations<String,String> opt=redisTemplate.opsForValue();
        opt.increment("goodkey"+good_id);
        return true;

    }

    @Override
    public int getRetain(int good_id ) {
        ValueOperations<String,String> opt=redisTemplate.opsForValue();
        //这个逻辑要注意，如果当前redis种的间不存在，就证明这个商品有被秒杀过

        return Integer.parseInt(opt.get("goodkey_"+good_id));
    }

    @Override
    public boolean getUserByGood(int good_id ,int user_id) {
        ValueOperations<String,String> opt=redisTemplate.opsForValue();
        String myvalue= opt.get(good_id+"_success_"+user_id);
        if(myvalue!=null){
            return true;
        }else {
            return false;
        }
    }


}

