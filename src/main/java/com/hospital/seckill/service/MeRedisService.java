package com.hospital.seckill.service;

import java.util.List;

public interface MeRedisService {
    //记录数据库库存
    public void getRedisStock(List<Integer> good_id, List<Integer> good_count);
    //在库存的基础上增加秒杀
    public  boolean redisSeckill(int good_id,int user_id);
    //加一个退单成功的接口
    public boolean backSeckill(int good_id);

//    public int getRedisStock(int good_id);
//获取剩余库存
    public int getRetain(int good_id);
    public  boolean getUserByGood(int good_id,int user_id);

}
