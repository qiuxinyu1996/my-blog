package com.qiuxinyu.handle;

import com.qiuxinyu.common.Result;
import com.qiuxinyu.exception.BadRequestException;
import com.qiuxinyu.exception.ForbiddenException;
import com.qiuxinyu.exception.InternalServerException;
import com.qiuxinyu.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice("com.qiuxinyu.controller")
public class ExceptionHandle {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result badParamException(BadRequestException e) {
        return Result.fail("400", e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result unAuthException(UnauthorizedException e) {
        return Result.fail("401", e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result badParamException(ForbiddenException e) {
        return Result.fail("403", e.getMessage());
    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result internalServerException(InternalServerException e) {
        return Result.fail("500", e.getMessage());
    }
}
