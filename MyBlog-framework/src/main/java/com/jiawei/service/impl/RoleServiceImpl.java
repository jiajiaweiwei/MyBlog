package com.jiawei.service.impl;


import com.jiawei.domain.entity.Role;
import com.jiawei.mapper.RoleMapper;
import com.jiawei.service.RoleService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2024-03-18 22:24:07
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<String> selectRoleKeyByUserId(Long userId) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if(userId == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
    //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(userId);

    }
}
