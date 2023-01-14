package com.qiuxinyu.exception;

public class UnAuthException extends RuntimeException{
    public UnAuthException() {

    }

    public UnAuthException(String message) {
        super(message);
    }
}
