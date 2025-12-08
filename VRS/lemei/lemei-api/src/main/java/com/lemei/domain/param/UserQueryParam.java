package com.lemei.domain.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yq
 * @CLassName UserQueryParam
 * @Description
 * @date 2022/8/29 16:16
 **/
@Data
public class UserQueryParam {

    private String userType;

    private Long userId;

    private Integer roleId;
}
