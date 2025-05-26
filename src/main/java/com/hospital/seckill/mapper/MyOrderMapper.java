package com.hospital.seckill.mapper;

import com.hospital.seckill.beans.MyOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyOrderMapper {
     //查询所有的秒杀商品
     public  void reduceStock(int id);
     //产生新的订单接口
     public  void insertOrder(MyOrder myOrder);
}
