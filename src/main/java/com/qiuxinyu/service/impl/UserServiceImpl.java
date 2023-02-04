package com.qiuxinyu.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiuxinyu.common.Result;
import com.qiuxinyu.common.ErrorCode;
import com.qiuxinyu.exception.BadRequestException;
import com.qiuxinyu.exception.ForbiddenException;
import com.qiuxinyu.exception.InternalServerException;
import com.qiuxinyu.pojo.entity.User;
import com.qiuxinyu.pojo.param.GetPasswordVerifyParam;
import com.qiuxinyu.pojo.param.RegisterParam;
import com.qiuxinyu.pojo.param.RegisterVerifyParam;
import com.qiuxinyu.mapper.UserMapper;
import com.qiuxinyu.service.UserService;
import com.qiuxinyu.util.JWTUtils;
import com.qiuxinyu.util.RandomUtils;
import com.qiuxinyu.util.VerifyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Result login(User loginUser, HttpServletResponse response) {
        User dbUser = getUserByUsername(loginUser.getUsername());
        if (dbUser == null) {
            dbUser = getUserByMobile(loginUser.getUsername());
        }
        if (dbUser == null) {
            throw new ForbiddenException(ErrorCode.NO_USER.getMessage());
        }
        if (!dbUser.getPassword().equals(loginUser.getPassword())) {
            throw new ForbiddenException(ErrorCode.ERROR_PASSWORD.getMessage());
        }

        String token = JWTUtils.generateToken(loginUser);
        response.setHeader("Access-Control-Expose-Headers","token");
        response.setHeader("token",token);

        return Result.success("登录成功",dbUser.getUsername());
    }

    User getUserByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        queryWrapper.last("limit 1 ");
        return getOne(queryWrapper);
    }

    User getUserByMobile(String mobile) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getMobile, mobile);
        queryWrapper.last("limit 1 ");
        return getOne(queryWrapper);
    }

    @Override
    public Result register(HttpServletResponse response, RegisterParam param, BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            throw new BadRequestException(ErrorCode.PARAM_ERROR.getMessage());
        }

        // 用户名不允许重复
        if (getUserByUsername(param.getUsername()) != null ||
                getUserByMobile(param.getUsername()) != null) {
            throw new ForbiddenException(ErrorCode.EXIST_USERNAME.getMessage());
        }else if (getUserByMobile(param.getMobile()) != null) {
            throw new ForbiddenException(ErrorCode.EXIST_MOBILE.getMessage());
        }

        // 验证码错误
        if (!StrUtil.equals(param.getVerifyCode(), (String)redisTemplate.opsForValue().get(param.getMobile() + ":verifyCode"))) {
            throw new ForbiddenException(ErrorCode.ERROR_CODE.getMessage());
        }

        User user = new User();
        BeanUtils.copyProperties(param, user);
        user.setId(UUID.randomUUID().toString());
        if (!save(user)) {
            throw new ForbiddenException(ErrorCode.REGISTER_FAIL.getMessage());//
        }

        return Result.success("注册成功");
    }

    @Override
    public Result registerVerify(HttpServletResponse response,RegisterVerifyParam param) {
        String verifyCode = RandomUtils.getVerifyCode(6);
        try {
            // 阿里云短信服务未返回成功信息
            if (!VerifyUtils.sendVerifyCode(param.getMobile(), verifyCode)) {
                throw new InternalServerException(ErrorCode.SEND_VERIFY_CODE_FAIL.getMessage());//
            }
        } catch (Exception e) {
            throw new InternalServerException(ErrorCode.SEND_VERIFY_CODE_FAIL.getMessage());//
        }
        // 请求过于频繁
        if (redisTemplate.opsForValue().get(param.getMobile() + ":history") != null) {
            throw new InternalServerException(ErrorCode.SEND_VERIFY_CODE_FAIL.getMessage());//
        }
        // 1分钟之内再次请求不响应
        redisTemplate.opsForValue().set(param.getMobile() + ":history", new Date().toString(), 1, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(param.getMobile() + ":verifyCode", verifyCode,5,TimeUnit.MINUTES);
        return Result.success();
    }

    @Override
    public Result getPasswordVerify(HttpServletResponse response, GetPasswordVerifyParam param) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getMobile, param.getMobile());
        queryWrapper.last("limit 1 ");
        User user = getOne(queryWrapper);
        if (user == null) {
            throw new ForbiddenException(ErrorCode.NO_USER.getMessage());//
        }
        // 验证码错误
        String verifyCode = (String) redisTemplate.opsForValue().get(param.getMobile() + ":verifyCode");

        if (!StrUtil.equals(param.getVerifyCode(), verifyCode)) {
            throw new ForbiddenException(ErrorCode.ERROR_CODE.getMessage());//
        }
        return Result.success(user.getPassword());
    }

}
