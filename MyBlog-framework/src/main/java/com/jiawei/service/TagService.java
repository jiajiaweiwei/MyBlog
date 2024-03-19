package com.jiawei.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Tag;
import com.jiawei.domain.to.TagListTo;

/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2024-03-18 19:33:40
 */
public interface TagService extends IService<Tag> {
    //分页查询标签
    ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListTo tagListTo);
}
