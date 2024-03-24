package com.jiawei.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Role;
import com.jiawei.domain.to.InsertRoleTo;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2024-03-18 22:06:22
 */
public interface RoleService extends IService<Role> {
    //根据用户id查询角色信息
    List<String> selectRoleKeyByUserId(Long id);


    //根据角色名称（模糊查询）和状态分页查询角色管理
    ResponseResult listByPageAndRoleName(Integer pageNum, Integer pageSize, String roleName,Integer status);

    //新增角色
    // 1.获取菜单树 在menuController中
    //2.插入角色信息 (改两个表 sys_role 和sys_role_menu )
    ResponseResult insertRole(InsertRoleTo insertRoleTo);
}
