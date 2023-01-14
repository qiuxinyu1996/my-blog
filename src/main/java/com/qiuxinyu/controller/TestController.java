package com.qiuxinyu.controller;

import com.qiuxinyu.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TestController {
    @GetMapping("/test")
    public Result test(HttpServletRequest request,HttpServletResponse response) {
        return Result.success();
    }
}
