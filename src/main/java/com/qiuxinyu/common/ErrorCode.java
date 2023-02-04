package com.qiuxinyu.common;

public enum ErrorCode {
    NO_USER("403","用户名或密码错误"),
    ERROR_PASSWORD("403","用户名或密码错误"),
    REGISTER_FAIL("403","注册失败"),
    EXIST_USERNAME("403","该用户名已存在"),
    EXIST_MOBILE("403","该手机号已注册，请直接登录"),
    NOT_LOGIN("403","未登录"),
    SEND_VERIFY_CODE_FAIL("403","验证码发送失败"),
    PARAM_ERROR("403","参数格式错误"),
    ERROR_CODE("403","验证码错误");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
