package com.lemei.domain.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yq
 * @CLassName LoginParam
 * @Description
 * @date 2022/8/26 15:21
 **/
@Data
public class LoginParam {

    @NotBlank(message = "用户名不能为空!")
    private String phone;
    @NotBlank(message = "密码不能为空")
    private String password;
}
