package com.hospital.seckill.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecKill {

    private int id;
    private String good_name;
    private double good_price;
    private int good_count;
    private String good_start_time;
    private String good_end_time;

}
