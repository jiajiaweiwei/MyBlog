package com.jiawei.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelCategoryVo {

    //生成excel的实体类
    @ExcelProperty("分类名称")
    private String name;
    @ExcelProperty("分类描述")
    private String description;
    @ExcelProperty("状态（0表示使用中，1表示已弃用）")
    private String status;

}
