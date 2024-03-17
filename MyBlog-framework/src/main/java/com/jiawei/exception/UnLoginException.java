package com.jiawei.exception;


import com.jiawei.enums.AppHttpCodeEnum;



// TODO 自己写的暂时不要乱用


//用户发表评论前 未登录的异常
public class UnLoginException extends RuntimeException {

    private int code;
    private String msg;
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
    public UnLoginException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }

}
