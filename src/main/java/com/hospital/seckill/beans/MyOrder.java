package com.hospital.seckill.beans;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyOrder implements Serializable {

    private int id;
    private int good_id;
    private int user_id;
    private int mycount;
    private int mysum;
    private String address;
    private String tel;

}
