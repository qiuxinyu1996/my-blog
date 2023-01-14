package com.qiuxinyu.handle;

import com.qiuxinyu.common.ErrorCode;
import com.qiuxinyu.common.Result;
import com.qiuxinyu.exception.UnAuthException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice("com.qiuxinyu.controller")
public class ExceptionHandle {

    @ExceptionHandler(UnAuthException.class)
    public Result unAuthException(UnAuthException e) {
        return Result.fail(ErrorCode.NOT_LOGIN);
    }
}
