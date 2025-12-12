package com.vrs.controller;

import com.vrs.common.core.domain.AjaxResult;
import com.vrs.common.core.domain.entity.SysUser;
import com.vrs.common.core.domain.model.LoginUser;
import com.vrs.common.utils.SecurityUtils;
import com.vrs.common.utils.StringUtils;
import com.vrs.domain.param.LoginParam;
import com.vrs.domain.param.UserEditParam;
import com.vrs.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author yq 用户类
 * @CLassName UserController
 * @Description
 * @date 2022/8/26 15:15
 **/
@RestController
@RequestMapping(value = "/api/user")
@Api(description = "小程序用户",tags = "login")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login",produces = "application/json;charset=UTF-8")
    public Object login(@RequestBody @Validated LoginParam param){
        return userService.userLogin(param.getPhone(),param.getPassword());
    }

    @PostMapping(value = "/userinfo",produces = "application/json;charset=UTF-8")
    @ApiOperation("查询用户信息")
    public Object getUserInfo(){
        return AjaxResult.success(userService.getUserInfo());
    }

    @PostMapping(value = "/update",produces = "application/json;charset=UTF-8")
    @ApiOperation("修改个人信息")
    public Object updateUserInfo(@RequestBody @Validated UserEditParam param){
        return AjaxResult.success(userService.updateUserInfo(param));
    }
    @ApiOperation("司机/承运商列表")
    @GetMapping(value = "/getUserList")
    public Object getUserList(@RequestParam("userType") String userType, @Param("parentId") Integer parentId){
        if (StringUtils.isEmpty(userType)){
            return AjaxResult.error("用户类型不能为空");
        }
        if ("02".equals(userType)){
            if (parentId==null){
                return AjaxResult.error("所属承运商id必传");
            }
        }
        return AjaxResult.success(userService.getUsersByUserType(userType,parentId,null,null));
    }

    @ApiOperation("审核人列表")
    @GetMapping(value = "/getCheckerList")
    public Object getCheckerList(@RequestParam("roleId")@NotNull(message = "角色id不能为空") Integer roleId){
        //roleId  固定144
        try {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            if (loginUser == null) {
                return AjaxResult.error("用户未登录");
            }
            Long userId = loginUser.getUserId();
            System.out.println("当前用户ID: " + userId + ", 线程: " + Thread.currentThread().getName());
            return AjaxResult.success(userService.getUsersByUserType(null,null,roleId,userId));
        } catch (Exception e) {
            System.err.println("获取用户信息失败: " + e.getMessage());
            return AjaxResult.error("获取用户信息失败，请重新登录");
        }
    }


    @ApiOperation("修改密码")
    @PostMapping(value = "/updatePassword")
    public Object updatePassword(@RequestBody UserEditParam param){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        param.setUserId(loginUser.getUserId());
        //原密码加密比对
        if (!SecurityUtils.matchesPassword(param.getPrePassword(),loginUser.getUser().getPassword())){
            return AjaxResult.error("原密码不正确");
        }
        param.setPassword(SecurityUtils.encryptPassword(param.getPassword()));
        return AjaxResult.success(userService.updateUserInfo(param));
    }

}
