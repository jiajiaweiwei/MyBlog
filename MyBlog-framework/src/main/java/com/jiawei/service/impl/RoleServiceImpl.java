package com.jiawei.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Role;
import com.jiawei.domain.entity.RoleMenu;
import com.jiawei.domain.to.InsertRoleTo;
import com.jiawei.domain.vo.PageVo;
import com.jiawei.mapper.RoleMapper;
import com.jiawei.service.RoleMenuService;
import com.jiawei.service.RoleService;
import com.jiawei.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2024-03-18 22:24:07
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    @Autowired
    private RoleMenuService roleMenuService;


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



    //根据角色名称（模糊查询）和状态分页查询角色管理
    @Override
    public ResponseResult listByPageAndRoleName(Integer pageNum, Integer pageSize, String roleName,Integer status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        queryWrapper.eq(!Objects.isNull(status),Role::getStatus,status);
        Page<Role> rolePage = new Page<>();
        rolePage.setSize(pageSize);
        rolePage.setCurrent(pageNum);
        page(rolePage,queryWrapper);
        PageVo pageVo = new PageVo(rolePage.getRecords(),rolePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }
    //新增角色
    // 1.获取菜单树 在menuController中
    //2.插入角色信息 (改两个表 sys_role 和sys_role_menu )
    @Transactional
    @Override
    public ResponseResult insertRole(InsertRoleTo insertRoleTo) {
        //新增的角色id自动生成
        Role role = BeanCopyUtils.copyBean(insertRoleTo, Role.class);
        save(role);
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getRoleName,role.getRoleName());
        Long newRoleId = getOne(queryWrapper).getId();
        List<Long> menuIds = insertRoleTo.getMenuIds();
        LinkedList<RoleMenu> roleMenus = new LinkedList<>();
        for (int i = 0; i < menuIds.size(); i++) {
            roleMenus.add(new RoleMenu(newRoleId, menuIds.get(i)));
        }
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    //3.修改
    @Transactional
    @Override
    public ResponseResult put(InsertRoleTo insertRoleTo) {
        //获取role对象
        Role role = BeanCopyUtils.copyBean(insertRoleTo, Role.class);
        //更新角色表信息
        updateById(role);
        //先将role_menu表 再重新 的相关联记录全部删除再重新插入
        //删除所有的
        LambdaQueryWrapper<RoleMenu> roleMenuWrapper = new LambdaQueryWrapper<>();
        roleMenuWrapper.eq(RoleMenu::getRoleId,insertRoleTo.getId());
        roleMenuService.remove(roleMenuWrapper);
        //重新插入新的菜单信息
        List<Long> menuIds = insertRoleTo.getMenuIds();
        LinkedList<RoleMenu> roleMenus = new LinkedList<>();
        for (int i = 0; i < menuIds.size(); i++) {
            roleMenus.add(new RoleMenu(insertRoleTo.getId(), menuIds.get(i)));
        }
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }


}
