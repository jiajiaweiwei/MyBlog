package com.jiawei;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@SpringBootTest
//@ConfigurationProperties(prefix = "oss")
public class OSSTest {
    private String accessKey;
    private String secretKey;
    private String bucket;
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
    @Test
    public void testOss(){
//����һ����ָ�� Region �����������
        Configuration cfg = new Configuration(Region.autoRegion());
//...���������ο���ע��
        UploadManager uploadManager = new UploadManager(cfg);
//...�����ϴ�ƾ֤��Ȼ��׼���ϴ�
// String accessKey = "your access key";
// String secretKey = "your secret key";
// String bucket = "sg-blog";
//Ĭ�ϲ�ָ��key������£����ļ����ݵ�hashֵ��Ϊ�ļ���
        String key = "2022/cat.png";
        try {
// byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
// ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes); "D:\�����ļ�\ͼƬ�ز�\cat.png"
            //�ϴ��ļ�����������·��
            InputStream inputStream = new FileInputStream("D:\\cat.png");
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response =
                        uploadManager.put(inputStream,key,upToken,null, null);
//�����ϴ��ɹ��Ľ��
                DefaultPutRet putRet = new
                        Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
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
        }
        }
