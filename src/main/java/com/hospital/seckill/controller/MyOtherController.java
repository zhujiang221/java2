package com.hospital.seckill.controller;

import com.hospital.admin.mapper.MyCommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MyOtherController {


    @Autowired
    MyCommonMapper myCommonMapper;
    @RequestMapping("/getanswer")
    public String get_answer(int id, Model model){
        myCommonMapper.selectyizhu(id);

        model.addAttribute("answer",myCommonMapper.selectyizhu(id));
        return "answer";
    }

}
