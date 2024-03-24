package com.jiawei.controller;

import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Article;
import com.jiawei.domain.to.AddArticleDto;
import com.jiawei.service.ArticleService;
import com.jiawei.utils.BeanCopyUtils;
import com.jiawei.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "博客管理") //防止与标签新增冲突
@RestController
@RequestMapping("/content/article")
public class ArticleController {




    @Autowired
    private ArticleService articleService;

    //发布博客
    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){ //注意@RequestBody不要导错包了
        return articleService.add(article);
    }



    //后台查询博客
    /*
    * 请求格式
    * pageNum 页码
    * pageSize 每页条数  模糊查询的需求
    * title 文章标题 模糊查询的需求
    * summary 文章摘要
    *
    * */
    @GetMapping("/list")
    public ResponseResult<Article> articleList(Integer pageNum, Integer pageSize, String title, String summary){
        return articleService.articleListByAdmin(pageNum,pageSize,title,summary);
    }



    //后台修改博客
    /*
     * 请求格式
     * pageNum 页码
     * pageSize 每页条数  模糊查询的需求
     * title 文章标题 模糊查询的需求
     * summary 文章摘要
     * 先根据id查询回显
     *
     * 再put请求修改
     * */


    //回显
    @GetMapping({"{id}"})
    public ResponseResult selectById(@PathVariable("id") Integer articleId){
        return articleService.adminGetArticleById(articleId);
    }
    //put更新博客信息
    @PutMapping()
    public ResponseResult updateArticleById(@RequestBody AddArticleDto articleDto){
        return articleService.updateArticleById(articleDto);
    }


    //删除文章 逻辑删除
    @DeleteMapping("{id}")
    public ResponseResult deleteById(@PathVariable("id") String articleId){
        //同时满足批量删除和单个删除
        //使用mybatis-plus逻辑删除
        String[] idArray = articleId.split(",");
        List<Long> idList = new ArrayList<>();
        for (String idStr : idArray) {
            idList.add(Long.parseLong(idStr));
        }
        return ResponseResult.okResult(articleService.removeByIds(idList));
    }

}
