package com.jiawei.controller;


import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Menu;
import com.jiawei.enums.AppHttpCodeEnum;
import com.jiawei.exception.SystemException;
import com.jiawei.service.MenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "菜单管理")
@RestController
@RequestMapping("/system/menu")
public class MenuController {



    @Autowired
    private MenuService menuService;

    //管理员查询菜单
    @GetMapping("list")
    public ResponseResult list(){
        return ResponseResult.okResult(menuService.list());
    }


    //管理员新增菜单
    @PostMapping
    public ResponseResult insertMenu(@RequestBody Menu menu){
        return ResponseResult.okResult(menuService.save(menu));
    }

    //管理员修改菜单 1.回显
    @GetMapping({"{id}"})
    public ResponseResult update1(@PathVariable("id") Long menuId){
        return ResponseResult.okResult(menuService.getById(menuId));

    }
    //管理员修改菜单 2.修改
    @PutMapping()
    public ResponseResult updateById(@RequestBody Menu menu){
        if (menu.getParentId().equals(menu.getId())){
            throw new SystemException(AppHttpCodeEnum.MENU_PARENT_ERROR);
        }
        return ResponseResult.okResult(menuService.updateById(menu));
    }

    //管理员删除菜单
    @DeleteMapping({"{id}"})
    public ResponseResult deleteMenuById(@PathVariable("id") Long menuId){
        return ResponseResult.okResult(menuService.removeById(menuId));
    }


    //新增角色方法
    // 1.获取菜单树 在menuController中 2.插入角色数据 在roleController中
    @GetMapping("/treeselect")
    public ResponseResult treeselect(){
        //使用MenuChildrenVo类 分装数据
        return menuService.treeselect();
    }
    //修改角色方法回显时的   第二部   查询列表树（根据不同角色，列表树不同）
    //2.加载对应角色菜单列表树接口(在menuController中)      查询对应角色的菜单树
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeSelect(@PathVariable("id") Long roleId){
        return menuService.roleMenuTreeselect(roleId);
    }







}
