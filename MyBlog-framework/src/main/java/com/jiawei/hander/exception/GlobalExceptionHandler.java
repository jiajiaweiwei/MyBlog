package com.jiawei.hander.exception;


import com.jiawei.domain.ResponseResult;
import com.jiawei.enums.AppHttpCodeEnum;
import com.jiawei.exception.SystemException;
import com.jiawei.exception.UnLoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /*
    * @RestControllerAdvice  controller全局异常处理
        @Slf4j    开启日志输出
    *
    * */
    //从抛出的异常中捕获
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        //打印异常信息
        log.error("出现了异常！{}",e);
        //从异常对象中获取提示信息 封装返回
        return ResponseResult.errorResult(e.getCode(),e.getMsg());

    }

    //未登录异常
    @ExceptionHandler(UnLoginException.class)
    public ResponseResult unLoginException(UnLoginException e){
        //打印异常信息
        log.error("出现了异常！{}",e);
        System.out.println("用户没有登录");
        //从异常对象中获取提示信息 封装返回
        return ResponseResult.errorResult(e.getCode(),e.getMessage());

    }


    //意料外的异常处理
    @ExceptionHandler(Exception.class)
    public ResponseResult exception(Exception e){
        //打印异常信息
        log.error("出现了异常！{}",e);
        //从异常对象中获取提示信息 封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());

    }







}
