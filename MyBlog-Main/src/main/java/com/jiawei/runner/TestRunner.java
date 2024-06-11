package com.jiawei.runner;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//测试在springboot项目启动后  立即执行一些代码
@Component
public class TestRunner implements CommandLineRunner {



    @Override
    public void run(String... args) throws Exception {
        System.out.println("----------启动！ ---------");


    }
}
