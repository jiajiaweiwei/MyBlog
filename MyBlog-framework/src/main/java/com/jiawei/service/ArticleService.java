package com.jiawei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Article;
import com.jiawei.domain.to.AddArticleDto;
import org.springframework.web.bind.annotation.RestController;

public interface ArticleService extends IService<Article> {
    //首页文章查询
    ResponseResult<Article> hotArticleList();

    //前台点击分类跳转分类页面
    //携带分类id 页码，一页记录数
    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);
    //根据id获取文章详情
    ResponseResult getArticleDetail(Long id);
    //文章浏览量增加时 自动更新redis的数据
    ResponseResult updateViewCount(Long id);


    //新增加博客
    ResponseResult add(AddArticleDto articleDto);

    //后台管理员查询文章数据
    ResponseResult<Article> articleListByAdmin(Integer pageNum, Integer pageSize,String title, String summary);

    //管理员修改文章回显
    ResponseResult adminGetArticleById(Integer articleId);

    //put更新博客信息
    ResponseResult updateArticleById(AddArticleDto articleDto);
}
