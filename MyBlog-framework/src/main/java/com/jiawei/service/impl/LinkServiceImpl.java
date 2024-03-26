package com.jiawei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiawei.constants.SystemConstants;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Link;
import com.jiawei.domain.vo.LinkVo;
import com.jiawei.domain.vo.PageVo;
import com.jiawei.mapper.LinkMapper;
import com.jiawei.service.LinkService;
import com.jiawei.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2024-03-14 23:23:48
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {


    //查询所有友链
    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> lambdaQueryWrapper = new  LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> linkList = list(lambdaQueryWrapper);
        //转化成VO
        List<LinkVo> linkVoes = BeanCopyUtils.copyBeanList(linkList, LinkVo.class);
        //封装返回
        return ResponseResult.okResult(linkVoes);
    }
    //分页查询所有友好链接
    @Override
    public ResponseResult listAll(Integer pageNum, Integer pageSize, String name,String status) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Link::getDelFlag,SystemConstants.STATUS_NORMAL);
        wrapper.eq(Link::getStatus,SystemConstants.STATUS_NORMAL);
        wrapper.like(StringUtils.hasText(name),Link::getName,name);
        Page<Link> linkPage = new Page<>();
        page(linkPage,wrapper);
        return ResponseResult.okResult(new PageVo(linkPage.getRecords(),linkPage.getTotal()));
    }



}
