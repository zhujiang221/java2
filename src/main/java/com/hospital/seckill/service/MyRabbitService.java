package com.hospital.seckill.service;

import com.hospital.seckill.beans.MyOrder;

public interface MyRabbitService {

    //两个接口一个接口是减库存，一个接口是订单添加
    public void reduceStock(int id);

    //产生新的订单接口
    public void insertOrder(MyOrder myOrder);
}
