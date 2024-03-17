package com.jiawei.controller;

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
    public ResponseResult userInfo(){
        return userService.userInfo();
    }


    //用户中心信息更新
    @PutMapping("/userInfo")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    //用户注册
    @PostMapping("/register")
    public ResponseResult rigister(@RequestBody User user){
        return userService.register(user);
    }


















}
