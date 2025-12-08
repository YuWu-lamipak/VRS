package com.lemei.domain.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yq
 * @CLassName UserQueryParam
 * @Description
 * @date 2022/8/30 09:29
 **/
@Data
public class UserEditParam {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "身份证号")
    private String idcard;

    @ApiModelProperty(value = "个人简介")
    private String personalProfile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "原密码")
    private String prePassword;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "地址")
    private String place;

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    private String sex;
}
