package com.jiawei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
//博客后端前台启动类
@SpringBootApplication
@MapperScan("com.jiawei.mapper")
@EnableScheduling  //开启定时任务
public class BlogMain {
    public static void main(String[] args) {

        SpringApplication.run(BlogMain.class,args);
    }
}
