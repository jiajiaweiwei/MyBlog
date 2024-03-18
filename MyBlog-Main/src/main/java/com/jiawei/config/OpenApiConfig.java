package com.jiawei.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: knife4j配置类
 **/
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                // 接口文档标题
                .info(new Info().title("MyBlogAPI framework接口文档")
                        // 接口文档简介
                        .description("这是基于Knife4j OpenApi3的接口文档")
                        // 接口文档版本
                        .version("v1.0")
                        // 开发者联系方式
                        .contact(new Contact().name("jiawei").email("jiawei.2001@qq.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("一个基于spring Boot + Mybatis+redis+OSS对象存储的的博客项目")
                        .url("暂时没有设置"));
    }

    /**自定义分组和资源映射*/
    @Bean
    public GroupedOpenApi articleApi() {
        return GroupedOpenApi.builder().group("文章模块")
                .pathsToMatch("/article/**")
                .build();
    }

    @Bean
    public GroupedOpenApi loginApi() {
        return GroupedOpenApi.builder().group("用户登录模块")
                .pathsToMatch("/login")
                .build();
    }
    @Bean
    public GroupedOpenApi logoutApi() {
        return GroupedOpenApi.builder().group("用户注销模块")
                .pathsToMatch("/logout")
                .build();
    }

    @Bean
    public GroupedOpenApi categoryApi() {
        return GroupedOpenApi.builder().group("查询文章分类信息")
                .pathsToMatch("/category/**")
                .build();
    }

    @Bean
    public GroupedOpenApi commentApi() {
        return GroupedOpenApi.builder().group("用户评论模块")
                .pathsToMatch("/comment/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder().group("用户模块")
                .pathsToMatch("/user/**")
                .build();
    }
    @Bean
    public GroupedOpenApi linkApi() {
        return GroupedOpenApi.builder().group("友链模块")
                .pathsToMatch("/link/**")
                .build();
    }

    @Bean
    public GroupedOpenApi OssApi() {
        return GroupedOpenApi.builder().group("OSS文件上传模块")
                .pathsToMatch("/upload/**")
                .build();
    }




}
