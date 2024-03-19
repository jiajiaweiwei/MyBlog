package com.jiawei.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Tag;
import com.jiawei.domain.to.TagListTo;
import com.jiawei.domain.vo.PageVo;
import com.jiawei.mapper.TagMapper;
import com.jiawei.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2024-03-18 19:33:40
 */
@Service
@Transactional
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {


    @Autowired
    private TagMapper tagMapper;

    //分页查询标签  TagListTo tagListTo为以后根据标签名查询做准备
    @Override
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListTo tagListTo) {
        //使用mybatis-Plus实现分页查询集合
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //根据标签名分类查询文章
        tagLambdaQueryWrapper.eq(StringUtils.hasText(tagListTo.getName()),Tag::getName,tagListTo.getName());
        tagLambdaQueryWrapper.eq(StringUtils.hasText(tagListTo.getRemark()),Tag::getRemark,tagListTo.getRemark());
        Page<Tag> tagPage = new Page<>();
        tagPage.setSize(pageSize);
        tagPage.setCurrent(pageNum);
        page(tagPage,tagLambdaQueryWrapper);
        //封装数据返回
        //pageVo 封装一个集合 一个记录总数
        PageVo pageVo = new PageVo(tagPage.getRecords(),tagPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }


}
