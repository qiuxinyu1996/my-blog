package com.qiuxinyu.controller;

import com.qiuxinyu.common.Result;
import com.qiuxinyu.pojo.entity.User;
import com.qiuxinyu.pojo.param.GetPasswordVerifyParam;
import com.qiuxinyu.pojo.param.RegisterParam;
import com.qiuxinyu.pojo.param.RegisterVerifyParam;
import com.qiuxinyu.service.UserService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpServletResponse response) {
        return userService.login(user,response);
    }

    @PostMapping("/register")
    public Result register(HttpServletResponse response, @Valid @RequestBody RegisterParam param, BindingResult bindingResult) {
        return userService.register(response, param,bindingResult);
    }

    @PostMapping("/register/verify")
    public Result registerVerify(HttpServletResponse response,@RequestBody RegisterVerifyParam param) {
        return userService.registerVerify(response, param);
    }

    @PostMapping("/getPassword/verify")
    public Result verify(HttpServletResponse response,@RequestBody GetPasswordVerifyParam param) {
        return userService.getPasswordVerify(response, param);
    }
}
