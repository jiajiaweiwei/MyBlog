package com.jiawei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//博客后台后端启动类
@SpringBootApplication
@MapperScan("com.jiawei.mapper")
public class BlogAdminApplication {
public static void main(String[] args) {
SpringApplication.run(BlogAdminApplication.class, args);
}
}
