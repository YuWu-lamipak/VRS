package com.lemei.service;

import com.lemei.common.core.domain.ResultData;
import com.lemei.common.core.domain.entity.SysUser;
import com.lemei.domain.param.UserEditParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yq
 * @CLassName UserService
 * @Description
 * @date 2022/8/26 15:17
 **/
public interface UserService {

    Object userLogin( String phone,String password);

    /**
     * 根据身份证号查询用户信息
     * @param idcard
     * @return
     */
    SysUser getDetailByIdcard(String idcard);

    /**
     * 获取当前用户信息
     * @return
     */
    SysUser getUserInfo();
    SysUser getUserInfoById(Long userId);

    /**
     * 修改用户信息
     * @param param
     * @return
     */
    int updateUserInfo(UserEditParam param);

    /**
     * 获取不同用户类型、角色的列表
     * @param userType
     * @return
     */
    List<ResultData> getUsersByUserType(String userType,Integer parentId,Integer roleId,Long userId);

}
