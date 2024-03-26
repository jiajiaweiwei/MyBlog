package com.jiawei.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiawei.domain.entity.UserRole;
import com.jiawei.mapper.UserRoleMapper;
import com.jiawei.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2024-03-26 15:40:56
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {



}
