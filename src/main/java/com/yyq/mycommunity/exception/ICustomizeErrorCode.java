package com.yyq.mycommunity.exception;


/*
* 返回异常错误码的接口，目的是为了当出现异常时，可以返回 code 和 message信息
*
* 定义两个方法，一个是获取信息，一个是获取错误码
*
* */
public interface ICustomizeErrorCode {
    String getMessage();
    Integer getCode();


}
