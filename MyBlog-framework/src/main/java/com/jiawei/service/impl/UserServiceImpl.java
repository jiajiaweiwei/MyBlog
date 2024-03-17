package com.jiawei.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.User;
import com.jiawei.domain.vo.UserInfoVo;
import com.jiawei.enums.AppHttpCodeEnum;
import com.jiawei.exception.SystemException;
import com.jiawei.mapper.UserMapper;
import com.jiawei.service.UserService;
import com.jiawei.utils.BeanCopyUtils;
import com.jiawei.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-03-16 13:08:12
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {



    @Autowired
    private PasswordEncoder passwordEncoder;


    //用户中心信息查询
    @Override
    public ResponseResult userInfo() {
        //获取用户id、
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装用户信息UserInfoVO返回
        UserInfoVo  userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    //更新用户信息
    @Override
    public ResponseResult updateUserInfo(User user) {
        //判断昵称是否被占用 true 表示已被占用
        if (userNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //后端也要验证数据不能为空
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }


        //用户注册  用户名密码不可以重复  邮箱密码可以重复
        //判断用户名是否被占用 true 表示已被占用
        if (userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //判断昵称是否被占用 true 表示已被占用
        if (nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }

        //密码加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);

        //创建时间
        Date date = new Date();
        user.setCreateTime(date);
        //用户性别等还没完善 默认为男
        user.setSex("0");


        //注册
        save(user);
        return ResponseResult.okResult();
    }

    //判断用户名是否被占用 被占用了返回true
    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        long count = count(queryWrapper);
        return count == 1;
    }

    //判断昵称是否被占用 被占用了返回true
    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        long count = count(queryWrapper);
        return count == 1;
    }










}
