package com.jiawei.controller;

import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.LoginUser;
import com.jiawei.domain.entity.Menu;
import com.jiawei.domain.entity.User;
import com.jiawei.domain.vo.AdminUserInfoVo;
import com.jiawei.domain.vo.RoutersVo;
import com.jiawei.domain.vo.UserInfoVo;
import com.jiawei.enums.AppHttpCodeEnum;
import com.jiawei.exception.SystemException;
import com.jiawei.service.AdminLoginService;
import com.jiawei.service.MenuService;
import com.jiawei.service.RoleService;
import com.jiawei.utils.BeanCopyUtils;
import com.jiawei.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "后台用户管理接口")
@RestController
public class AdminLoginController {

    //管理员登录service
    @Autowired
    private AdminLoginService adminLoginService;
    //权限菜单service
    @Autowired
    private MenuService menuService;
    //角色
    @Autowired
    private RoleService roleService;


    //用户登录
    @Operation(summary = "后台用户登录")
    @PostMapping("/user/login")
    public ResponseResult login(@Parameter(allowEmptyValue = false,description = "用户登录要的用户名和密码") @RequestBody User user){
        //使用自定义的全局异常
        if(!StringUtils.hasText(user.getUserName())){
            //提示，必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        //判断 对请求参数的合法性
        return adminLoginService.login(user);
    }


    //admin后端获取用户  权限等信息
    @Operation(summary = "后台获取用户权限信息")
    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //封装为admin User Info返回
        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        //封装数据返回
        AdminUserInfoVo adminUserInfoVo = new
                AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    //查询路由
    @Operation(summary = "后台获取用户路由信息")
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }


    //用户退出登录
    @Operation(summary = "后台退出用户登录，并从redis中删除用户token等各种信息")
    @PostMapping("user/logout")
    public ResponseResult logout(){
        return adminLoginService.logout();
    }








}
