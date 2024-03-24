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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.RuntimeErrorException;
import java.io.UnsupportedEncodingException;
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


}
