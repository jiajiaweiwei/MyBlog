package com.jiawei.controller;


import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.User;
import com.jiawei.enums.AppHttpCodeEnum;
import com.jiawei.exception.SystemException;
import com.jiawei.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class BlogLoginController {


    @Autowired
    private BlogLoginService blogLoginService;
    //用户接口
    //用户登录
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        //使用自定义的全局异常
        if(!StringUtils.hasText(user.getUserName())){
          //提示，必须要传用户名
          throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        //判断 对请求参数的合法性
        return blogLoginService.login(user);
    }

    //注销登录
    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }




}
