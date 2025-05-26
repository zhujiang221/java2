package com.hospital.seckill.service.impl;


import com.hospital.seckill.beans.SecKill;
import com.hospital.seckill.mapper.SeckillMapper;
import com.hospital.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private SeckillMapper seckillMapper;

    @Override
    public List<SecKill> findAll() {
        return seckillMapper.findAll();
    }
}
