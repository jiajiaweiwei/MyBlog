package com.jiawei.service;

import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.User;
import com.jiawei.domain.vo.AdminUserInfoVo;

public interface AdminLoginService {
    //后台用户登录
    ResponseResult login(User user);
    //admin后端获取用户  权限等信息 使用封装好的工具类

    //后台退出用户登录，并从redis中删除用户token等各种信息
    ResponseResult logout();

}
