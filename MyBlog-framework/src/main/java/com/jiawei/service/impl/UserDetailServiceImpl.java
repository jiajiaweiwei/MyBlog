package com.jiawei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jiawei.constants.SystemConstants;
import com.jiawei.domain.entity.LoginUser;
import com.jiawei.domain.entity.User;
import com.jiawei.mapper.MenuMapper;
import com.jiawei.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;


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
        // TODO 只有后台用户才会去查询权限信息 前台用户直接放行
        if (user.getType().equals(SystemConstants.ADMIN)){
            List<String> strings = menuMapper.selectPermsByUserId(user.getId());//查询出权限集合
            return new LoginUser(user,strings);
        }
        return new LoginUser(user,null);
    }
}
