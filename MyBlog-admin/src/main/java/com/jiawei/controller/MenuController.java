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


    // 1.获取菜单树 在menuController中
    @GetMapping("/treeselect")
    public ResponseResult treeselect(){
        //使用MenuChildrenVo类 分装数据
        return menuService.treeselect();
    }


}
