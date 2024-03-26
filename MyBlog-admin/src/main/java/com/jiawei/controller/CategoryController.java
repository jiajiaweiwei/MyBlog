package com.jiawei.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.Category;
import com.jiawei.domain.vo.CategoryVo;
import com.jiawei.domain.vo.ExcelCategoryVo;
import com.jiawei.enums.AppHttpCodeEnum;
import com.jiawei.service.CategoryService;
import com.jiawei.utils.BeanCopyUtils;
import com.jiawei.utils.WebUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.RuntimeErrorException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


@Tag(name = "分类管理") //防止与标签新增冲突
@RequestMapping("/content/category")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @Operation(summary = "查询所有分类")
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return ResponseResult.okResult(categoryService.listAllCategory());
    }


    @Operation(summary = "将分类信息下载成excel保存") //返回文件数据
    @PreAuthorize("@ps.hasPermission('content:category:export') ")//自定义权限校验
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        //设置下载文件的请求头
        try {
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> list = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(list, ExcelCategoryVo.class);
            //把数据写入到excel中
            EasyExcel.write(response.getOutputStream(),
                            ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            //出现异常需要响应json数据
            //如果出现异常也要响应json
            ResponseResult result =
                    ResponseResult.errorResult(AppHttpCodeEnum.FILE_UPDATE_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    //分页查询文章列表
    @Operation(summary = "后台分页查询文章列表") //返回文件数据
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, String name,Integer status){
        return categoryService.listByPage(pageNum,pageSize,name,status);
    }

    //新增分类
    @Operation(summary = "后台新增分类") //返回文件数据
    @PostMapping
    public ResponseResult insertCategory(@RequestBody Category category){
        return ResponseResult.okResult(categoryService.save(category));
    }

    //修改分类 1.回显
    @Operation(summary = "修改分类回显") //返回文件数据
    @GetMapping("{id}")
    public ResponseResult update(@PathVariable("id") Long categoryId){
        return ResponseResult.okResult(categoryService.getById(categoryId));
    }
    //修改分类 2.点击修改
    @Operation(summary = "点击修改") //返回文件数据
    @PutMapping
    public ResponseResult put(@RequestBody  Category category){
        return ResponseResult.okResult(categoryService.updateById(category));
    }

    //删除分类
    @Operation(summary = "删除分类") //返回文件数据 (逻辑删除) 可以批量删除
    @DeleteMapping("{id}")
    public ResponseResult del(@PathVariable("id") String categoryId){
        //同时满足批量删除和单个删除
        //使用mybatis-plus逻辑删除
        String[] idArray = categoryId.split(",");
        List<Long> idList = new ArrayList<>();
        for (String idStr : idArray) {
            idList.add(Long.parseLong(idStr));
        }
        return ResponseResult.okResult(categoryService.removeByIds(idList));
    }


}
