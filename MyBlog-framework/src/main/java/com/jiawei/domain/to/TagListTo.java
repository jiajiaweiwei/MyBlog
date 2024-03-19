package com.jiawei.domain.to;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//接受前端的tagList To类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagListTo {


    //标签名字
    @Schema(description = "接受前端的tagList To类 标签名字") //swagger的文档注释
    private String name;
    //标签备注
    @Schema(description = "接受前端的tagList To类 标签备注") //swagger的文档注释
    private String remark;




}
