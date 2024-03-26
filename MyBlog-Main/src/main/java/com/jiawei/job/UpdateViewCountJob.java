package com.jiawei.job;

import com.jiawei.domain.entity.Article;
import com.jiawei.service.ArticleService;
import com.jiawei.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


//定时任务
@Component
public class UpdateViewCountJob  {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;


    //要求每十分钟总执行redis文章浏览数据更新到MySQL综中
    @Transactional
    @Scheduled(cron = "0/60 * * * * ?") //有七个部分组成，中间以空格分隔
    public  void testJob(){
        //要执行的代码
        System.out.println("每60秒执行redis文章浏览数据更新到MySQL综中");
        //获取redis中的浏览量数据
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");
        //生成对文章类
        //使用mybatis-plus的updateBatchById方法批量更新 忽略空属性
        List<Article> articleList = viewCountMap.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到数据库中
        articleService.updateBatchById(articleList);
    }


}
