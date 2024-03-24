package com.jiawei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiawei.constants.SystemConstants;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Article;
import com.jiawei.domain.entity.ArticleTag;
import com.jiawei.domain.entity.Category;
import com.jiawei.domain.entity.Tag;
import com.jiawei.domain.to.AddArticleDto;
import com.jiawei.domain.vo.ArticleDetailVo;
import com.jiawei.domain.vo.ArticleListVo;
import com.jiawei.domain.vo.HotArticleVo;
import com.jiawei.domain.vo.PageVo;
import com.jiawei.mapper.ArticleMapper;
import com.jiawei.mapper.ArticleTagMapper;
import com.jiawei.mapper.TagMapper;
import com.jiawei.service.ArticleService;
import com.jiawei.service.ArticleTagService;
import com.jiawei.service.CategoryService;
import com.jiawei.service.TagService;
import com.jiawei.utils.BeanCopyUtils;
import com.jiawei.utils.RedisCache;
import com.jiawei.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {


    @Autowired
    @Lazy
    private CategoryService categoryService;


    @Autowired
    private RedisCache redisCache;


    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    //首页文章查询
    @Override
    public ResponseResult<Article> hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多查询十条
        Page<Article> page = new Page<>(1,5);
        page(page,queryWrapper);

        List<Article> records = page.getRecords();


        for (Article articleDetail:
             records) {
            //浏览量从redis中获取
            //从redis中获取viewCount
            Integer viewCount = redisCache.getCacheMapValue("article:viewCount", articleDetail.getId().toString());
            articleDetail.setViewCount(viewCount.longValue());

        }




        //bean拷贝
        //使用工具类拷贝集合
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(records, HotArticleVo.class);
        return ResponseResult.okResult(hotArticleVos);
    }
    //点击分类跳转分类页面
    //携带分类id 页码，一页记录数
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询条件（单表，没有封装分类名称）
        //如果有categoryId查询时就要和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId>0,Article::getCategoryId,categoryId);
        //状态是已经发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.STATUS_NORMAL);
        //对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        //VO封装
        //封装分类名称 再查另外一个表
        List<Article> articles = page.getRecords();
        //articleId去查询articleName进行配置
        //方法2 流式编程
        articles = articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        for (ArticleListVo articleDetail:
                articleListVos) {
            //浏览量从redis中获取
            //从redis中获取viewCount
            Integer viewCount = redisCache.getCacheMapValue("article:viewCount", articleDetail.getId().toString());
            articleDetail.setViewCount(viewCount.longValue());
        }

        //分页VO
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }
    //根据id获取文章详情
    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id获取文章详情
        Article articleDetail = getById(id);
        //浏览量从redis中获取
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount",
                id.toString());
        articleDetail.setViewCount(viewCount.longValue());
        //转化成VO
        ArticleDetailVo articleDetailVO = BeanCopyUtils.copyBean(articleDetail, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVO.getCategoryId();
        Category categoryServiceById = categoryService.getById(categoryId);
        if (categoryServiceById != null){
            articleDetailVO.setCategoryName(categoryServiceById.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVO);
    }




    //文章浏览量增加时 自动更新redis的数据
    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应文章的浏览量
        //article:viewCount redis中map对象
        //id为map的键
        //1 表示给浏览量加1
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }





    //新增加博客 添加事务的注解，避免当上传博客时 可能会有标签被删除的异常
    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        //使用stream 将文章与标签多对多的关系存入数据库
        //过滤获取
        List<ArticleTag> articleTags = articleDto.getTags().stream().map(tarId -> new ArticleTag(article.getId(), tarId))
                .collect(Collectors.toList());
        //存入数据库
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }


    //后台管理员查询博客数据  可以模糊查询
    @Override
    public ResponseResult<Article> articleListByAdmin(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(summary),Article::getSummary,summary);
        queryWrapper.like(StringUtils.hasText(title),Article::getTitle,title);
        Page<Article> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNum);
        page(page,queryWrapper);
        //pageVo 封装一个集合 一个记录总数
        return ResponseResult.okResult(new PageVo(page.getRecords(), page.getTotal()));
    }



    //管理员修改文章回显
    @Override
    public ResponseResult adminGetArticleById(Integer articleId) {
        Article article = getById(articleId);
        AddArticleDto articleDto = BeanCopyUtils.copyBean(article, AddArticleDto.class);
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, articleId);
        List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
        List<Long> tagId = articleTags.stream().map(articleTag -> articleTag.getTagId()).collect(Collectors.toList());
        articleDto.setTags(tagId);
        return ResponseResult.okResult(articleDto);
    }





    //put更新博客信息
    @Transactional
    @Override
    public ResponseResult updateArticleById(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        //使用stream 将文章与标签多对多的关系存入数据库
        //过滤获取
        List<ArticleTag> articleTags = articleDto.getTags().stream().map(tarId -> new ArticleTag(article.getId(), tarId))
                .collect(Collectors.toList());
        //先全部删除，再用plus集体插入 因为tag article关联表没有主键 所以没法更新 只能先删再插
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,articleDto.getId());
        articleTagMapper.delete(articleTagLambdaQueryWrapper);
        //集体插入
        //再用plus集体插入
        articleTagService.saveBatch(articleTags);
        //更新文章信息
        /*article.setUpdateTime(new Date()); 不用写 在mybatis配置中自动填充
        article.setUpdateBy(SecurityUtils.getUserId());*/
        article.setUpdateBy(SecurityUtils.getUserId());
        updateById(article);
        return ResponseResult.okResult();
    }

}
