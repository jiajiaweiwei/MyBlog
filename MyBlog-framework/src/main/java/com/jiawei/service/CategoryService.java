package com.jiawei.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Category;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2024-03-10 23:31:07
 */
public interface CategoryService extends IService<Category> {
    //获取文章分类
    ResponseResult getCategoryList();




}
