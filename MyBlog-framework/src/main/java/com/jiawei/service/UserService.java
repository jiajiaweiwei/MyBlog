package com.jiawei.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2024-03-16 13:08:12
 */
public interface UserService extends IService<User> {

    //用户中心 信息查询
    ResponseResult userInfo();

    //更新用户信息
    ResponseResult updateUserInfo(User user);
    //用户注册
    ResponseResult register(User user);
}
