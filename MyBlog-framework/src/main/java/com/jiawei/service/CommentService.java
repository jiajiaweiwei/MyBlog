package com.jiawei.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Comment;

/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2024-03-16 11:23:41
 */
public interface CommentService extends IService<Comment> {

    //根据文章id分页查询评论
    ResponseResult commentList(String type, Long articleId, Integer pageNum, Integer pageSize);

    //用户发表评论
    ResponseResult addComment(Comment comment);
}
