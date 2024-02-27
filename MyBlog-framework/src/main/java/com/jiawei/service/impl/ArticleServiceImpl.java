package com.jiawei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiawei.domain.entity.Article;
import com.jiawei.mapper.ArticleMapper;
import com.jiawei.service.ArticleService;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
}
