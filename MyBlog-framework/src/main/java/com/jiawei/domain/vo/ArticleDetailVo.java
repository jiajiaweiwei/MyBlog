package com.jiawei.domain.vo;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//文章详情VO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailVo {

    //响应给前端的VO字段
    @TableId
    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    //所属分类
    private String categoryName;
    //所属分类id
    private Long categoryId;
    //文章内容
    private String content;
    //缩略图
    private String thumbnail;
    //访问量
    private Long viewCount;

    private Date createTime;


}
