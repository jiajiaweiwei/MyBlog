package com.jiawei.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2024-03-18 21:47:27
 */
public interface MenuService extends IService<Menu> {
    //根据用户id查询权限信息
    List<String> selectPermsByUserId(Long id);
    //查询menu 结果是tree的形式 vue动态路由的信息
    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    //新增角色 1.获取菜单树
    ResponseResult treeselect();
    //修改角色方法回显时的   列表树（根据不同角色，列表树不同）
    //2.加载对应角色菜单列表树接口(在menuController中)      查询对应角色的菜单树
    ResponseResult roleMenuTreeselect(Long roleId);
}
