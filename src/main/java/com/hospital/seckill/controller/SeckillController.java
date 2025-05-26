package com.hospital.seckill.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.hospital.seckill.beans.MyOrder;

import com.hospital.seckill.beans.SecKill;
import com.hospital.seckill.config.AiPayconfig;
import com.hospital.seckill.service.MeRedisService;
import com.hospital.seckill.service.SeckillService;
import com.hospital.seckill.service.impl.MyRabbitPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/")
public class SeckillController {

    @Autowired
     private AiPayconfig mypayconfig;
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private MeRedisService myRedisService;
    @Autowired
    private MyRabbitPublisher myRabbitPublisher;
//    @Autowired
//    private MyRedisService myRedisService;

    @RequestMapping ("/seckill")
    public  String get_seckill(Model model){
        List<SecKill> mygoods=seckillService.findAll();
        //构建遍历Mygoods的对象中的所有good_id和 good_count
        List<Integer> mygoodids=new ArrayList<>();
        List<Integer> mygoodcounts=new ArrayList<>();
//        少1个初始状态
        List<Boolean> mysuccess=new ArrayList<>();
        for(int i=0;i<mygoods.size();i++){
            mysuccess.add(false);
        }
        //遍历mygoods中的所有的ids
        for(SecKill mygood:mygoods){
            mygoodids.add(mygood.getId());
            mygoodcounts.add(mygood.getGood_count());
        }
        //这里只需要把mygoods中的库存取出，放在redis
        myRedisService.getRedisStock(mygoodids,mygoodcounts);
        model.addAttribute("goods",mygoods);
        model.addAttribute("success",mysuccess);
        return "seckill";
    }
    /*
    秒杀成功后，前端有 库存变化
     */
    @GetMapping("/create_seckill")
    public  String create_other_seckill(int id,Model model){

        //在controller的redisSeckill的秒杀方法中先给一个同样的user_id
        boolean result= myRedisService.redisSeckill(id,1);
        //定义秒杀成功的列表
        List<Boolean> mysuccess=new ArrayList<>();
        //从数据库中取数据
        List<SecKill> mygoods=seckillService.findAll();

        if(result){
            for(SecKill mygood:mygoods){

                mygood.setGood_count(myRedisService.getRetain(mygood.getId()));

            }
        }
        for(SecKill mygood:mygoods){
            //从redis调用秒杀成功的可能性
            mysuccess.add(myRedisService.getUserByGood(mygood.getId(),1));
        }
        System.out.println("=================================");
        System.out.println(mysuccess);
        System.out.println(mygoods);
        System.out.println("--------------------------------=");
        model.addAttribute("goods",mygoods);
        model.addAttribute("success",mysuccess);
        return "seckill";
    }
    //写一个支付成功的CONtroller
    @GetMapping("/pay_order" )
    public  String  pay_order(int good_id,int user_id,double price,Model model){
        model.addAttribute("good_id",good_id);
        model.addAttribute("user_id",user_id);
        int myprice=(int)price;
        model.addAttribute("price",myprice);
        return  "pay";
    }
    //支付失败，退单
    @RequestMapping("/back_order")
    public String back_order(int good_id){
        myRedisService.backSeckill(good_id);
        return"redirect:/seckill";

    }
    @RequestMapping("/success")
    public String getsuccess(){
        return "success";
    }
    @RequestMapping( value = "/pay",method = {RequestMethod.GET,RequestMethod.POST})
    public void payment(MyOrder myOrder, HttpServletResponse response) {
        System.out.println("------------123123123123123--------");
        AlipayClient myclient= new DefaultAlipayClient(
                "https://openapi-sandbox.dl.alipaydev.com/gateway.do",
                mypayconfig.getAppId(),mypayconfig.getAppPrivateKey(),"JSON","UTF-8",
                mypayconfig.getAlipayPublic(),"RSA2"
                );
        AlipayTradePagePayRequest request =new AlipayTradePagePayRequest();
        request.setNotifyUrl("");
        request.setReturnUrl("http://localhost:8082/success");
        //调用alibaba
        request.setNotifyUrl(mypayconfig.getNotifyUrl());
        String myid= UUID.randomUUID().toString().replaceAll( "-","");
        String content=("{\"out_trade_no\":\""+myid+"\",\"total_amount\":\""+myOrder.getMysum()+
                "\",\"subject\":\""+"体检优惠券"+"\",\"product_code\":"+"\"FAST_INSTANT_TRADE_PAY\"}");
        request.setBizContent(content);
        System.out.println(content);
        String form = "";
        try {

            form= myclient.pageExecute(request).getBody();
            System.out.println(form);
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(form);
            response.getWriter().flush();
            response.getWriter().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("------------123123123123123--------");
        System.out.println("----------------------------");
        System.out.println(myOrder);
        myRabbitPublisher.payment(myOrder);

    }


    @GetMapping("/many_seckill")
    public  String create_many_seckill(){
        //直接随机即可
        int user_id=(int) Math.floor(Math.random()*100+1);
        System.out.println(user_id);
        boolean result=myRedisService.redisSeckill(1,user_id);
        return "seckill";
    }


}
