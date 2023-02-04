package com.qiuxinyu.intercept;

import com.qiuxinyu.common.ErrorCode;
import com.qiuxinyu.exception.UnauthorizedException;
import com.qiuxinyu.util.JWTUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 预请求直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        // 拦截校验用户是否登录
        if (JWTUtils.resolveToken(request.getHeader("token"))) {
            return true;
        }
        throw new UnauthorizedException(ErrorCode.NOT_LOGIN.getMessage());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
