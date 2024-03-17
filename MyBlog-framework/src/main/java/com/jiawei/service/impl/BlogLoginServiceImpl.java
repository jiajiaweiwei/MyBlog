package com.jiawei.service.impl;

import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.LoginUser;
import com.jiawei.domain.entity.User;
import com.jiawei.domain.vo.BlogUserLoginVo;
import com.jiawei.domain.vo.UserInfoVo;
import com.jiawei.service.BlogLoginService;
import com.jiawei.utils.BeanCopyUtils;
import com.jiawei.utils.JwtUtil;
import com.jiawei.utils.RedisCache;
import com.mysql.cj.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {



    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    //用户登录
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
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);
        //使用VO 封装token userInfo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt,userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
    }


    //用户注销登录
    @Override
    public ResponseResult logout() {
        //获取token中的用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject("bloglogin:"+userId);
        return new ResponseResult(200,"注销成功");
    }


    //注销登录








}
