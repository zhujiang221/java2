package com.hospital.seckill.mapper;

import com.hospital.seckill.beans.SecKill;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SeckillMapper {
     List<SecKill> findAll();
}
