package com.jiawei.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Role;
import com.jiawei.domain.entity.RoleMenu;
import com.jiawei.domain.entity.User;
import com.jiawei.domain.entity.UserRole;
import com.jiawei.domain.vo.PageVo;
import com.jiawei.domain.vo.UserInfoVo;
import com.jiawei.enums.AppHttpCodeEnum;
import com.jiawei.exception.SystemException;
import com.jiawei.mapper.UserMapper;
import com.jiawei.service.RoleService;
import com.jiawei.service.UserRoleService;
import com.jiawei.service.UserService;
import com.jiawei.utils.BeanCopyUtils;
import com.jiawei.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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


    //封装公用的注册方法部分
    public void postUserPublic(User user){
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
        //用户性别等前端还没完善
        //注册（save(user);）操作在具体业务函数中
    }


    @Override
    public ResponseResult register(User user) {
        postUserPublic(user);
        save(user);
        //前台注册 默认为普通用户
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





    //后台查询数据库所有的用户 包括逻辑删除
    @Override
    public ResponseResult listByPage(Integer pageNum, Integer pageSize, String userName,String status,String phonenumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        queryWrapper.like(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        queryWrapper.eq(!Objects.isNull(status),User::getStatus,status);
        Page<User> userPage = new Page<>();
        userPage.setSize(pageSize);
        userPage.setCurrent(pageNum);
        page(userPage,queryWrapper);
        //隐藏密码
        userPage.getRecords().stream().forEach(user -> user.setPassword("臭小子别抓包了"));
        PageVo pageVo = new PageVo(userPage.getRecords(),userPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }



    @Autowired
    private UserRoleService userRoleService;
    //新增用户 1.回显显示可关联的角色 在roleController中
    // 2.新增用户 注意密码加密存储
    //两表及以上的修改 添加事务管理
    @Transactional
    @Override
    public ResponseResult adminPostUser(User user) {
        //公用的用户注册信息
        postUserPublic(user);
        save(user);
        List<Long> roleIds = user.getRoleIds();
        //更新完用户表再获取他的用户id
        Long userId = user.getId();
        //后台注册绑定用户与指定role的关系 更新role_user表
        List<UserRole> userRoles = new LinkedList<>();
        for (int i = 0; i < roleIds.size(); i++) {
            userRoles.add(new UserRole(userId, roleIds.get(i)));
        }
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }


    @Autowired
    private RoleService roleService;
    //修改用户
    //1.回显用户信息
    @Override
    public ResponseResult getUserBeforePut(Long userId) {
        //获取所有角色信息
        List<Role> roleAllList = roleService.list();
        //获取该用户有的角色信息的角色id
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,userId);
        List<Long> roleIds = userRoleService.list(wrapper).stream().map(UserRole::getRoleId).collect(Collectors.toList());
        //获取用户信息
        User user = getById(userId);
        user.setPassword("看啥呢臭小子");
        //封装数据返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("roleIds",roleIds);
        map.put("roles",roleAllList);
        map.put("user",user);
        return ResponseResult.okResult(map);
    }
    //修改用户
    //2.更新用户信息
    @Transactional
    @Override
    public ResponseResult putUser(User user) {
        updateById(user);
        //更新用户权限
        //先删后插
        LambdaQueryWrapper<UserRole> wrapperDel = new LambdaQueryWrapper<>();
        wrapperDel.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(wrapperDel);
        //后插
        //这里前端有点bug，勾选了要更改的角色后在变动其他属性勾选的角色就会显示出来
        List<Long> roleIds = user.getRoleIds();
        LinkedList<UserRole> userRoles = new LinkedList<>();
        for (int i = 0; i < roleIds.size(); i++) {
            userRoles.add(new UserRole(user.getId(),roleIds.get(i)));
        }
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }


}
