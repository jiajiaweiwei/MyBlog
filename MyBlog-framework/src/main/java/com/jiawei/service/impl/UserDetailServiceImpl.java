package com.jiawei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jiawei.domain.entity.LoginUser;
import com.jiawei.domain.entity.User;
import com.jiawei.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {


    @Autowired
    private UserMapper userMapper;


    //用户登录
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(userQueryWrapper);
        //判断是否查询到用户，没有则抛出异常
        if (Objects.isNull(user)){
            System.out.println("用户名不存在");
            throw new RuntimeException("用户不存在");
        }
        //TODO 查询权限信息封装



        return new LoginUser(user);
    }
}
