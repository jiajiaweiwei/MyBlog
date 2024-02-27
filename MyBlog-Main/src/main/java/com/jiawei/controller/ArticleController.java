package com.jiawei.controller;

import com.jiawei.domain.entity.Article;
import com.jiawei.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    //测试
    @GetMapping("/list")
    public List<Article> test(){
        return articleService.list();
    }


















}
