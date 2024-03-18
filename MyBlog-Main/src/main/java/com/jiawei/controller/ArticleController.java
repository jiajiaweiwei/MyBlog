package com.jiawei.controller;

import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Article;
import com.jiawei.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.annotations.Target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "文章接口")
@RestController
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    //测试
    /*@GetMapping("/list")
    public List<Article> test(){
        return articleService.list();
    }*/

    //首页文章
    @Operation(summary = "首页热点文章分页查询")
    @GetMapping("/hotArticleList")
    public ResponseResult<Article> hotArticleList(){
        return articleService.hotArticleList();
    }

    //点击分类跳转分类页面
    //携带分类id 页码，一页记录数
    @Operation(summary = "分类查询文章")
    @GetMapping("/articleList")
    public ResponseResult articleList(@Parameter(allowEmptyValue = false,description = "分类查询所有文章的总页数pageNum") Integer pageNum, Integer pageSize, Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    //根据id获取文章详情
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }


    //文章浏览量增加时 自动更新redis的数据
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }












}
