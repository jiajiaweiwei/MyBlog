package com.jiawei.controller;


import com.jiawei.domain.ResponseResult;
import com.jiawei.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {


    @Autowired
    private UploadService uploadService;


    //文件上传到七牛云
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
    }





}
