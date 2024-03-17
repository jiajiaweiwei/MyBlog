package com.jiawei.job;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



//定时任务
@Component
public class TestJob {




    //每隔5秒执行代码
    @Scheduled(cron = "0/5 * * * * ?") //有七个部分组成，中间以空格分隔
    public  void testJob(){
        //要执行的代码
        System.out.println("测试 每隔5秒 定时任务执行");

    }



}
