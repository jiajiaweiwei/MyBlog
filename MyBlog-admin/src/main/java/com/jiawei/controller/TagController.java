package com.jiawei.controller;


import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Tag;
import com.jiawei.domain.to.TagListTo;
import com.jiawei.service.TagService;
import com.jiawei.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@io.swagger.v3.oas.annotations.tags.Tag(name = "标签管理") //防止与标签新增冲突
@RestController
@RequestMapping("/content/tag")
public class TagController {


    @Autowired
    private TagService tagService;

    @Operation(summary = "分页查询标签")
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, TagListTo tagListTo){
        return tagService.pageTagList(pageNum,pageSize,tagListTo);
    }

    //标签新增
    @Operation(summary = "标签新增")
    @PostMapping
    public ResponseResult insertTag(@RequestBody Tag tag){
        //需要手动传输新增标签的用户信息，和标签创建时间
        tag.setCreateBy(SecurityUtils.getUserId());
        tag.setUpdateBy(SecurityUtils.getUserId());
        Date date = new Date();
        tag.setCreateTime(date);
        tag.setUpdateTime(date);
        return ResponseResult.okResult(tagService.save(tag));
    }

    //标签删除

    //从 URL 路径中获取路径变量和 键值对传的参数 用@PathVariable("") 路径传参
    //HTTP 请求中获取请求参数的值 用param
    @Operation(summary = "标签删除")
    @DeleteMapping("{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long tagId){
        //使用mybatis-plus逻辑删除
        return ResponseResult.okResult(tagService.removeById(tagId));
    }



}
















