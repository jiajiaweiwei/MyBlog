package com.jiawei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Article;

public interface ArticleService extends IService<Article> {
    //首页文章查询
    ResponseResult<Article> hotArticleList();

    //点击分类跳转分类页面
    //携带分类id 页码，一页记录数
    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);
    //根据id获取文章详情
    ResponseResult getArticleDetail(Long id);
    //文章浏览量增加时 自动更新redis的数据
    ResponseResult updateViewCount(Long id);
}
