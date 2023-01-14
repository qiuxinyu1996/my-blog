package com.qiuxinyu.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class RegisterParam {
    private String username;
    private String password;
    private String mobile;
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
}
