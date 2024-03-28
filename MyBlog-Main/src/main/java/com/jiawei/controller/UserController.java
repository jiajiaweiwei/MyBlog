package com.jiawei.controller;

import com.jiawei.annotation.SystemLog;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.User;
import com.jiawei.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {







    @Autowired
    private UserService userService;

    //用户中心

    //用户中心信息查询
    @GetMapping("/userInfo")
    @SystemLog(BusinessName = "用户信息查询")  //指定切点和指定业务信息
    public ResponseResult userInfo(){
        return userService.userInfo();
    }


    //用户中心信息更新
    @PutMapping("/updateUserInfo")
    @SystemLog(BusinessName = "更新用户信息")  //指定切点和指定业务信息
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    //用户注册
    @PostMapping("/register")
    public ResponseResult rigister(@RequestBody User user){
        return userService.register(user);
    }


















}
