package com.qiuxinyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qiuxinyu.common.Result;
import com.qiuxinyu.pojo.entity.User;
import com.qiuxinyu.pojo.param.GetPasswordVerifyParam;
import com.qiuxinyu.pojo.param.RegisterParam;
import com.qiuxinyu.pojo.param.RegisterVerifyParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;

public interface UserService extends IService<User> {
    Result login(@RequestBody User user, HttpServletResponse response);

    Result register(HttpServletResponse response, RegisterParam param, BindingResult bindingResult);

    Result registerVerify(HttpServletResponse response, RegisterVerifyParam param);

    Result getPasswordVerify(HttpServletResponse response, GetPasswordVerifyParam param);
}
