package com.jiawei.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Link;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2024-03-14 23:23:48
 */
public interface LinkService extends IService<Link> {




    //查询所有友链
    ResponseResult getAllLink();

    //分页查询所有友好链接
    ResponseResult listAll(Integer pageNum, Integer pageSize, String name,String status);

}
