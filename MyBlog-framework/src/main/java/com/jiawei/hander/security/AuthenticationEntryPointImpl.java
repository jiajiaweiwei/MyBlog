package com.jiawei.hander.security;

import com.alibaba.fastjson.JSON;
import com.jiawei.domain.ResponseResult;
import com.jiawei.enums.AppHttpCodeEnum;
import com.jiawei.utils.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


//认证失败处理器
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        //打印日常信息
        authException.printStackTrace();
        ResponseResult result = null;
        //响应给前端认证失败信息
        if (authException instanceof BadCredentialsException){
            result =  ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),authException.getMessage());
        }else if (authException instanceof InsufficientAuthenticationException){
            //就是这里出的异常 要使用mybatis的逻辑删除的方法
            result =  ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else {
            result =  ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"未知错误");
        }



        //根据不同一场返回不同错误信息
        // 没有token的异常 InsufficientAuthenticationException
        //BadCredentialsException 密码错误的异常
        //RuntimeException用户不存在的异常
        WebUtils.renderString(response, JSON.toJSONString(result));






    }
}
