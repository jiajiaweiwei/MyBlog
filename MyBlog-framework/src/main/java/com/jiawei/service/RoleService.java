package com.jiawei.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiawei.domain.entity.Role;

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
}
