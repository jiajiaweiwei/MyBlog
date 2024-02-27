package com.jiawei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jiawei.mapper")
public class BlogMain {
    public static void main(String[] args) {
        SpringApplication.run(BlogMain.class,args);
    }
}
