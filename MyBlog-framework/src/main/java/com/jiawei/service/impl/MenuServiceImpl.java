package com.jiawei.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiawei.constants.SystemConstants;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Menu;
import com.jiawei.domain.entity.RoleMenu;
import com.jiawei.mapper.MenuMapper;
import com.jiawei.service.MenuService;
import com.jiawei.service.RoleMenuService;
import com.jiawei.utils.SecurityUtils;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2024-03-18 21:47:27
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {





    //根据用户id查询权限信息
    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if(id == 1L){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            //获取menu集合的权限字段，封装为集合
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则查询普通用户所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }






    //查询menu 结果是tree的形式 vue动态路由的信息
    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
        //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
        //否则 获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        //先找出第一层的菜单 然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    //TODO 重点 使用递归实现获取多层级的动态路由
    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }
    /**
     * 获取存入参数的 子Menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }



    //获取所有菜单树公用方法
    public List<Menu> pub(){
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
        //先查父角色
        List<Menu> list = list(wrapper);//所有的菜单
        //设置label
        list.stream().forEach(menu->menu.setLabel(menu.getMenuName()));
        //获取顶级菜单 第一层 例：系统管理
        List<Menu> grandfathers = list.stream().
                filter(menuVoes -> menuVoes.getParentId().equals(0L)).
                collect(Collectors.toList());
        //设置第二层children 例：用户管理
        grandfathers.stream().forEach(menuVoes->menuVoes.setChildren(list.stream().filter(listOne->listOne.getParentId().equals(menuVoes.getId())).collect(Collectors.toList())));
        //还要设置第三层children 例：用户修改
        List<List<Menu>> fathers = grandfathers.stream().map(collect1 -> collect1.getChildren()).collect(Collectors.toList());
        //set第三层属性 例子 用户修改
        fathers.stream().forEach(father->father.stream().forEach(fa->fa.setChildren(list.stream().filter(li->li.getParentId().equals(fa.getId())).collect(Collectors.toList()))));
        return grandfathers;
    }



    //新增角色 1.获取菜单树 2.增加新角色 在roleController中
    @Override
    public ResponseResult treeselect() {
        return ResponseResult.okResult(pub());
    }
    //修改角色方法回显时的   列表树（根据不同角色，列表树不同）
    //2.加载对应角色菜单列表树接口(在menuController中)      查询对应角色的菜单树
    @Autowired
    private RoleMenuService roleMenuService;
    //修改方法回显菜单树
    @Override
    public ResponseResult roleMenuTreeselect(Long roleId) {


        //如果是超级管理员，返回所有的权限
        if (roleId == 1L){
            List<Menu> menus = pub();
            //封装数据
            HashMap hashMap = new HashMap();
            hashMap.put("menus",menus);//所有的树
            List<Long> Ids = list().stream().filter(li -> li.getStatus() != "1").map(listOne -> listOne.getId()).collect(Collectors.toList());
            hashMap.put("checkedKeys",Ids);//关联的id
            return ResponseResult.okResult(hashMap);
        }

        //开始获取顶级菜单
        LambdaQueryWrapper<RoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuLambdaQueryWrapper.eq(RoleMenu::getRoleId,roleId);
        //拿到该角色的所有menu 及顶级菜单
        List<RoleMenu> roleMenus = roleMenuService.list(roleMenuLambdaQueryWrapper);
        //获取该顶级菜单里所有的menuId 且状态正常
        List<Long> menuIds = roleMenus.stream().map(roleMenu -> roleMenu.getMenuId()).collect(Collectors.toList());
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
        menuLambdaQueryWrapper.in(Menu::getId,menuIds);
        List<Menu> list = list(menuLambdaQueryWrapper);
        List<Long> menuIds2 = list.stream().map(li -> li.getId()).collect(Collectors.toList());
        //封装数据
        HashMap hashMap = new HashMap();
        hashMap.put("menus",pub());//所有的树
        hashMap.put("checkedKeys",menuIds2);//关联的id
        return ResponseResult.okResult(hashMap);

    }







}
