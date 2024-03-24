package com.jiawei.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiawei.domain.entity.ArticleTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-21 14:32:07
 */
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}
