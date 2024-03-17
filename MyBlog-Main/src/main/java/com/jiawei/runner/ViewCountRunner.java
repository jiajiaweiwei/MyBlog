package com.jiawei.runner;


import com.jiawei.domain.entity.Article;
import com.jiawei.mapper.ArticleMapper;
import com.jiawei.service.ArticleService;
import com.jiawei.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

//springboot项目启动后  将文章浏览量加载到redis
@Component
public class ViewCountRunner implements CommandLineRunner {


    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;


    @Override
    public void run(String... args) throws Exception {
        //应用启动时将文章浏览量加载到redis
        //获取所有文章
        List<Article> articles = articleMapper.selectList(null);
        //使用stream流筛选数据
        Map<String, Integer> viewCountMap = articles.
                stream()
                .collect(Collectors.toMap(article -> article.getId().toString(),
                        article -> article.getViewCount().intValue()));
        //存储到redis中
        redisCache.setCacheMap("article:viewCount",viewCountMap);
    }














}
