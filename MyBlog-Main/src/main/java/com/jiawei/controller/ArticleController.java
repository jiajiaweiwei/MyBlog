package com.jiawei.controller;

import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Article;
import com.jiawei.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/hotArticleList")
    public ResponseResult<Article> hotArticleList(){
        return articleService.hotArticleList();
    }

    //点击分类跳转分类页面
    //携带分类id 页码，一页记录数
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
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
