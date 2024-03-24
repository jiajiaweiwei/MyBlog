package com.jiawei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiawei.domain.entity.RoleMenu;
import com.jiawei.mapper.RoleMenuMapper;
import com.jiawei.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2024-03-24 23:49:22
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
}
