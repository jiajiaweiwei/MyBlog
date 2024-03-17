package com.jiawei.service.impl;

import com.google.gson.Gson;
import com.jiawei.domain.ResponseResult;
import com.jiawei.enums.AppHttpCodeEnum;
import com.jiawei.exception.SystemException;
import com.jiawei.service.UploadService;
import com.jiawei.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

@Service
@ConfigurationProperties(prefix = "oss")
@Data
public class OssUploadServiceImpl implements UploadService {


    private String accessKey;
    private String secretKey;
    private String bucket;




    //文件上传到七牛云
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //获取文件类型
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        //要求只能上传文件类
        if (!originalFilename.endsWith(".png")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //TODO 判断文件大小

        //判断通过

        //文件上传到七牛云
        //调用公共上传方法并获取返回的外链地址
        //自定义传输到对象存储的文件名
        //使用自定义工具类 根据原始文件名生成 存在对象存储的位置，根据日期存储
        String filePath = PathUtils.generateFilePath(originalFilename);//最终生成的文件位置
        String url = UploadOss(img,filePath);

        return ResponseResult.okResult(url);
    }

    //公共上传方法
    private String UploadOss(MultipartFile img,String filePath){


        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;//最终生成的文件位置
        try {
            //上传文件不能有中文路径
            //获取文件输入流
            InputStream inputStream = img.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response =
                        uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new
                        Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                //返回外链地址
                return "http://sahiydfcc.hd-bkt.clouddn.com/"+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        //返回外链地址
        return "出现了异常";

    }
}
