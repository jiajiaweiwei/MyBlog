package com.jiawei.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiawei.constants.SystemConstants;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Comment;
import com.jiawei.domain.vo.CommentVo;
import com.jiawei.domain.vo.PageVo;
import com.jiawei.enums.AppHttpCodeEnum;
import com.jiawei.exception.SystemException;
import com.jiawei.mapper.CommentMapper;
import com.jiawei.service.CommentService;
import com.jiawei.service.UserService;
import com.jiawei.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2024-03-16 11:23:42
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


    @Autowired
    private UserService userService;


    //根据文章id分页查询评论
    @Override
    public ResponseResult commentList(String type, Long articleId, Integer pageNum, Integer pageSize) {
        //查询文章对应评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //判断查询的是评论或者友好链接
        queryWrapper.eq(Comment::getType,type);

        //根据id查询文章
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(type),Comment::getArticleId,articleId);
        //并且为已发布状态
        //根评论id = -1
        queryWrapper.eq(Comment::getRootId,-1);
        //根评论按照评论发表时间排序，早发布的在前
        queryWrapper.orderByAsc(Comment::getCreateTime);
        //分页查询
        Page<Comment> commentPage = new Page<>(pageNum,pageSize);
        //分页查询
        page(commentPage,queryWrapper);
        List<CommentVo> commentVoList = toCommentVoList(commentPage.getRecords());
        //查询子评论
        for (CommentVo co :
                commentVoList) {
            List<CommentVo> children = getChildrenById(co.getId());
            co.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVoList,commentPage.getTotal()));
    }




    //根据根评论id查询子评论集合
    private List<CommentVo> getChildrenById(Long id){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        //对子评论进行时间排序
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        List<CommentVo> commentVoList = toCommentVoList(comments);
        return commentVoList;


    }

    private List<CommentVo> toCommentVoList(List<Comment> commentList){
        List<CommentVo> list = BeanCopyUtils.copyBeanList(commentList,CommentVo.class);
        //遍历vo集合
        //方法1 使用stream流
        //list.stream().map()
        //方法2 普通遍历
        for (CommentVo co :
                list) {
            //获取本评论的用户昵称
            String nickName = userService.getById(co.getCreateBy()).getNickName();
            String avatar = userService.getById(co.getCreateBy()).getAvatar();
            co.setUsername(nickName);
            co.setAvatar(avatar);
            //toCommentId为-1，表示这是根评论，没有子评论
            if (co.getToCommentUserId() != -1){
                //获取子评论的用户名
                String nickName1 = userService.getById(co.getToCommentUserId()).getNickName();
                String avatar1 = userService.getById(co.getCreateBy()).getAvatar();
                co.setToCommentUserName(nickName1);
                co.setAvatar(avatar);
            }
            //通过
        }
        //通过toCommentUserId查询用户的昵称并赋值
        return list;

    }


    //用户发表评论
    @Override
    public ResponseResult addComment(Comment comment) {
        //异常判断 ，防止直接调用接口，不携带参数
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.COMMENT_BOT_NULL);
        }
        //TODO
        /*//验证用户登录状态，如果没登录不能发表评论，响应对应异常
        if (SecurityUtils.getAuthentication() == null){
            throw new UnLoginException(AppHttpCodeEnum.NEED_LOGIN);
        }*/
        save(comment);
        return ResponseResult.okResult();
    }


}
