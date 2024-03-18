package com.jiawei.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiawei.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-18 22:06:23
 */
public interface RoleMapper extends BaseMapper<Role> {
    //否则查询用户所具有的角色信息
    List<String> selectRoleKeyByUserId(Long userId);
}
