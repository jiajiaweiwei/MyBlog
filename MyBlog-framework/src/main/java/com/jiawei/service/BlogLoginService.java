package com.jiawei.service;


import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;

public interface BlogLoginService {

    //用户登录
    ResponseResult login(User user);

    //注销登录
    ResponseResult logout();
}
