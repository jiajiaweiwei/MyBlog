package com.jiawei.controller;

import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Role;
import com.jiawei.domain.to.InsertRoleTo;
import com.jiawei.domain.to.RoleStatusDto;
import com.jiawei.domain.to.TagListTo;
import com.jiawei.enums.AppHttpCodeEnum;
import com.jiawei.exception.SystemException;
import com.jiawei.service.MenuService;
import com.jiawei.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;


    //根据角色名称（模糊查询）和状态分页查询角色管理
    @GetMapping("/list")
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName,Integer status){
        return roleService.listByPageAndRoleName(pageNum,pageSize,roleName,status);
    }

    //改变角色状态（可用与不可用）
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleStatusDto roleStatusDto){
        Long roleId = roleStatusDto.getRoleId();
        Role role = new Role();
        role.setId(roleId);
        role.setStatus(roleStatusDto.getStatus());
        return ResponseResult.okResult(roleService.updateById(role));
    }

    //新增角色
    // 1.获取菜单树 在menuController中
    //2.插入角色信息 (改两个表 sys_role 和sys_role_menu )
    @PostMapping
    public ResponseResult inserRole(@RequestBody InsertRoleTo insertRoleTo){
        return roleService.insertRole(insertRoleTo);
    }





}
