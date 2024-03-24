package com.jiawei.service.impl;

import com.jiawei.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;


//自定义权限控制
@Service("ps")
public class PermissionService {

    public boolean hasPermission(String permission){
        //如果是id为1 的超级管理员 直接返回true放行
        if (SecurityUtils.isAdmin()) return true;
        //否则获取当前用户的权限并判断是否存在permission
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);


    }


}
