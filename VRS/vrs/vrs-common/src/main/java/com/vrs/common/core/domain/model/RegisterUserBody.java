package com.vrs.common.core.domain.model;

import com.vrs.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;


/**
 * 前台用户注册
 */
public class RegisterUserBody extends BaseEntity {
    //@NotBlank(message = "手机号码不能为空")
    //@Size(min = 11,max = 11,message = "手机号码长度不正确")
    //@Pattern(regexp = "^(((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[6])|(17[0135678])|(18[0-9])|(19[89]))\\d{8})$", message = "手机号格式错误")
    private String phone;
    private String password;//密码
    @NotBlank(message = "验证码不能为空")
    private String code; //验证码
    private String uuid;//图形验证码
    private String username;//用户账户
    private String nickname;//用户名称
    private String personal_profile;// 个人简介
    private String imgUrl;//头像地址
    public String getImgUrl() {
        return imgUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPersonal_profile() {
        return personal_profile;
    }

    public void setPersonal_profile(String personal_profile) {
        this.personal_profile = personal_profile;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
