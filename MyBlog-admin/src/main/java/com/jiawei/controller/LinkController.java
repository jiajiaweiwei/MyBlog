package com.jiawei.controller;


import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Link;
import com.jiawei.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "后台链接管理")
@RestController
@RequestMapping("/content/link")
public class LinkController {


    @Autowired
    private LinkService linkService;

    //分页查询友好链接列表
    @Operation(summary = "分页查询所有友好链接")
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, String name,String status){
        return linkService.listAll(pageNum,pageSize,name,status);
    }

    //新增链接
    @Operation(summary = "新增链接")
    @PostMapping
    public ResponseResult postLink(@RequestBody Link link){
        return ResponseResult.okResult(linkService.save(link));
    }
    //修改链接
    @Operation(summary = "修改链接回显")
    @GetMapping("{id}")
    public  ResponseResult putLink(@PathVariable("id") Long linkId){
        return ResponseResult.okResult(linkService.getById(linkId));
    }
    //修改链接 2 点击修改
    @Operation(summary = "点击修改链接")
    @PutMapping
    public  ResponseResult putLink(@RequestBody Link link){
        return ResponseResult.okResult(linkService.updateById(link));
    }

    //删除链接 逻辑删除 要能多选
    @Operation(summary = "点击修改链接")
    @DeleteMapping("{id}")
    public  ResponseResult delLink(@PathVariable("id") String linkId){
        //同时满足批量删除和单个删除
        //使用mybatis-plus逻辑删除
        String[] idArray = linkId.split(",");
        List<Long> idList = new ArrayList<>();
        for (String idStr : idArray) {
            idList.add(Long.parseLong(idStr));
        }
        return ResponseResult.okResult(linkService.removeByIds(idList));
    }




}
