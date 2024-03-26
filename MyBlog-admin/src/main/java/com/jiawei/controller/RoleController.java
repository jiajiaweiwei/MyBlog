package com.jiawei.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jiawei.constants.SystemConstants;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Role;
import com.jiawei.domain.to.InsertRoleTo;
import com.jiawei.domain.to.RoleStatusDto;
import com.jiawei.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseResult insertRole(@RequestBody InsertRoleTo insertRoleTo){
        return roleService.insertRole(insertRoleTo);
    }



    //修改角色
    //1.修改角色回显 第一步查询
    @GetMapping("{id}")
    public ResponseResult update(@PathVariable("id") Long roleId){
        return ResponseResult.okResult(roleService.getById(roleId));
    }
    //2.加载对应角色菜单列表树接口(在menuController中)

    //3.修改
    @PutMapping
    public ResponseResult put(@RequestBody InsertRoleTo insertRoleTo){
        return roleService.put(insertRoleTo);
    }


    //删除角色 (逻辑删除 也不删除role_menu表中的数据)
    @DeleteMapping({"{id}"})
    public ResponseResult deleteByRoleId(@PathVariable("id") String roleId){
        //同时满足批量删除和单个删除
        //使用mybatis-plus逻辑删除
        String[] idArray = roleId.split(",");
        List<Long> idList = new ArrayList<>();
        for (String idStr : idArray) {
            idList.add(Long.parseLong(idStr));
        }
        return ResponseResult.okResult(roleService.removeByIds(idList));
    }



    //新增用户
    // 1.回显显示可关联的角色
    @Operation(summary = "新增用户之前的回显正常角色信息")  //在roleController中
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        return ResponseResult.okResult(roleService.list(queryWrapper));
    }





}
