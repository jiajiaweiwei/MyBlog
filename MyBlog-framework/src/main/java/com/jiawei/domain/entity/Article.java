package com.jiawei.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 文章表(Article)表实体类
 *
 * @author jiawei
 * @since 2024-02-27 17:45:39
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_article")
@Accessors(chain = true) //让set方法的返回值类型为这个实例对象本身
public class Article  {

    @Schema(description = "文章的主键") //swagger的文档注释
    @TableId
    private Long id;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章内容")
    private String content;

    @Schema(description = "文章摘要")
    private String summary;

    @Schema(description = "文章所属分类id")
    private Long categoryId;

    @Schema(description = "文章缩略图地址")
    private String thumbnail;

    @Schema(description = "文章是否置顶（0否，1是）")
    private String isTop;

    @Schema(description = "文章状态（0已发布，1草稿）")
    private String status;

    @Schema(description = "文章点击量")
    private Long viewCount;

    @Schema(description = "文章是否允许评论 1是，0否")
    private String isComment;

    @Schema(description = "文章作者id")
    @TableField(fill = FieldFill.INSERT) //在MyMetaObjectHandler中定义过了
    private Long createBy;


    @Schema(description = "文章创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "更新文章的作者id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @Schema(description = "文章更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Schema(description = "文章删除标志（0代表未删除，1代表已删除）")
    private Integer delFlag;

    //别的表的字段 不封装。只在bean拷贝时使用
    @Schema(description = "文章类型 别的表的字段 不封装。只在bean拷贝时使用")
    @TableField(exist = false)
    private String categoryName;










    //为将redis数据更新到MySQL使用
    public Article(Long id, long viewCount) {
        this.id = id;
        this.viewCount = viewCount;
    }


}

