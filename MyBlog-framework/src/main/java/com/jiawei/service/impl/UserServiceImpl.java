package com.jiawei.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.User;
import com.jiawei.domain.vo.UserInfoVo;
import com.jiawei.mapper.UserMapper;
import com.jiawei.service.UserService;
import com.jiawei.utils.BeanCopyUtils;
import com.jiawei.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-03-16 13:08:12
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {




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
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //用户注册
        //密码需要加密


        return null;
    }
}
