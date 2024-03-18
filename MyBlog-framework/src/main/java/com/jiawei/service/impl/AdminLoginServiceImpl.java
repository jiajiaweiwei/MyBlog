package com.jiawei.service.impl;

import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.LoginUser;
import com.jiawei.domain.entity.User;
import com.jiawei.domain.vo.AdminUserInfoVo;
import com.jiawei.service.AdminLoginService;
import com.jiawei.utils.JwtUtil;
import com.jiawei.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;


@Service
public class AdminLoginServiceImpl implements AdminLoginService {




    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;



    //后台用户登录
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断认证是否通过
        //用户名不存在或者密码错误的话会返回空
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("密码错误");
        }
        //获取用户id生成jwt
        LoginUser loginUser = (LoginUser)authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject("adminLogin:"+userId,loginUser);
        //后台管理不需要用户信息，只用token即可
        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    //admin后端获取用户  权限等信息
    @Override
    public ResponseResult<AdminUserInfoVo> getInfo() {






        return null;
    }


}
