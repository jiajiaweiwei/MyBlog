package com.jiawei.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiawei.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-18 21:47:27
 */
public interface MenuMapper extends BaseMapper<Menu> {
    //否则返回所具有的权限
    List<String> selectPermsByUserId(Long userId);
    //如果是 获取所有符合要求的Menu
    List<Menu> selectAllRouterMenu();
    //否则 获取当前用户所具有的Menu
    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}
