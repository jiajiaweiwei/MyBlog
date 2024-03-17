package com.jiawei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.jiawei.mapper")
@EnableScheduling  //开启定时任务
public class BlogMain {
    public static void main(String[] args) {

        System.out.println("原神，启动！");
        SpringApplication.run(BlogMain.class,args);
    }
}
