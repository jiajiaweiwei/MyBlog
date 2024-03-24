package com.jiawei.controller;


import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Tag;
import com.jiawei.domain.to.TagListTo;
import com.jiawei.service.TagService;
import com.jiawei.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        return ResponseResult.okResult(tagService.save(tag));
    }

    //标签删除

    //从 URL 路径中获取路径变量和 键值对传的参数 用@PathVariable("") 路径传参
    //HTTP 请求中获取请求参数的值 用param
    @Operation(summary = "标签删除")
    @DeleteMapping("{id}")
    public ResponseResult deleteTag(@PathVariable("id") String tagId){
        //同时满足批量删除和单个删除
        //使用mybatis-plus逻辑删除
        String[] idArray = tagId.split(",");
        List<Long> idList = new ArrayList<>();
        for (String idStr : idArray) {
            idList.add(Long.parseLong(idStr));
        }
        return ResponseResult.okResult(tagService.removeByIds(idList));
    }

    //标签修改 先get获取标签信息再修改
    @Operation(summary = "标签更新（修改）")
    @PutMapping
    public ResponseResult updateTag(@RequestBody Tag tag){
        return ResponseResult.okResult(tagService.updateById(tag));
    }
    //获取标签信息
    @Operation(summary = "获取标签信息")
    @GetMapping({"{id}"})
    public ResponseResult selectTag(@PathVariable("id") Long tagId){
        return ResponseResult.okResult(tagService.getById(tagId));
    }

    //写博客需求分析
    //1.查询所有标签和分类
    //2.上传图片获取图片访问地址
    //4.上传博文
    @Operation(summary = "写博文页面获取所有标签信息")
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return ResponseResult.okResult(tagService.list());
    }















}
















