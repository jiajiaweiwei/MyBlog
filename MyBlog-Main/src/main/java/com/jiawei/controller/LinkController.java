package com.jiawei.controller;


import com.jiawei.domain.ResponseResult;
import com.jiawei.service.impl.LinkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//友好链接
@RestController
@RequestMapping("link")
public class LinkController {



    @Autowired
    LinkServiceImpl linkService;



    //查询所有友链
    @GetMapping("getAllLink")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }




}
