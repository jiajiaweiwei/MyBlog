package com.jiawei.service;

import com.jiawei.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    //文件上传到七牛云
    ResponseResult uploadImg(MultipartFile img);
}
