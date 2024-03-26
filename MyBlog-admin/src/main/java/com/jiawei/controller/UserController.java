package com.jiawei.controller;


import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.User;
import com.jiawei.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "后台用户管理")
@RestController
@RequestMapping("/system/user")
public class UserController {



    @Autowired
    private UserService userService;

    //查询数据库所有的用户 包括逻辑删除 可以根据用户名和手机模糊查询
    @Operation(summary = "分页查询数据库所有的用户 包括逻辑删除 （把密码隐藏）")
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, String userName,String status,String phonenumber){
        return userService.listByPage(pageNum,pageSize,userName,status,phonenumber);
    }

    //修改用户状态
    @Operation(summary = "分页查询数据库所有的用户 包括逻辑删除 （把密码隐藏）")
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody User user){
        return ResponseResult.okResult(userService.updateById(user));
    }



    //新增用户 1.回显显示可关联的角色 在roleController中
    // 2.新增用户
    @Operation(summary = "新增用户 并在role_user中绑定角色信息")
    @PostMapping
    public ResponseResult adminPostUser(@RequestBody User user){
        //注意密码加密存储
        return userService.adminPostUser(user);
    }

    //删除用户 要可批量删除
    @Operation(summary = "逻辑删除用户信息")
    @DeleteMapping("{id}")
    public ResponseResult deleteUserById(@PathVariable("id") String userId){
        //同时满足批量删除和单个删除
        //使用mybatis-plus逻辑删除
        String[] idArray = userId.split(",");
        List<Long> idList = new ArrayList<>();
        for (String idStr : idArray) {
            idList.add(Long.parseLong(idStr));
        }
        return ResponseResult.okResult(userService.removeByIds(idList));
    }

    //修改用户
    //1.回显用户信息
    @Operation(summary = "修改用户信息回显")
    @GetMapping ("{id}")
    public ResponseResult getUserBeforePut(@PathVariable("id") Long userId){
        return userService.getUserBeforePut(userId);
    }

    //修改用户
    //2.更新用户信息
    @Operation(summary = "修改用户信息")
    @PutMapping
    public ResponseResult putUser(@RequestBody User user){
        return userService.putUser(user);
    }




}
