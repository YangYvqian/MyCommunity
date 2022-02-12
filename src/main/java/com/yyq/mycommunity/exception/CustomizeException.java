package com.yyq.mycommunity.exception;

/*
*  java的异常分两类，一种是error，一种是exception
*  exception 又包含 RuntimeException
*  此异常是处理运行时异常的问题
*
*
*  第一次系统的对异常进行实战，先总结一下，
*  此次项目的异常类用枚举的方式给出，因为异常出现需要返回  状态码code 和 异常信息message，可以用枚举类来封装
*  可以把异常信息都封装到枚举类中，统一调用，实现代码的复用
*
*/

public class CustomizeException extends RuntimeException{

    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
