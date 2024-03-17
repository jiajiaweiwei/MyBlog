package com.jiawei.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {
    //评论id
    @TableId
    private Long id;
    //文章id
    private Long articleId;
    //根评论id
    private Long rootId;
    //根评论用户名
    private String username;
    //评论内容
    private String content;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    //所回复的目标评论的userid
    private String toCommentUserName;
    //回复目标评论id
    private Long toCommentId;
    //回复的用户id
    private Long createBy;
    private Date createTime;


    //子评论
    private List<CommentVo> children;

}
