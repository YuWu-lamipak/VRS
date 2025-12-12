package com.vrs.mapper;


import com.vrs.common.core.domain.ResultData;
import com.vrs.common.core.domain.entity.SysUser;
import com.vrs.domain.User;
import com.vrs.domain.param.UserEditParam;
import com.vrs.domain.param.UserQueryParam;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户mapper层
 */
@Mapper
public interface UserMapper {

    /**
     * 根据手机号查询用户
     * @param phoneNumber
     * @return
     */
    SysUser getUserByPhone(String phoneNumber);
    /**
     * 根据用户名查询用户
     * @param userName
     * @return
     */
    SysUser getUserByUserName(String userName);

    /**
     * 根据查询条件查询用户信息
     * @param param
     * @return
     */
    SysUser selectUserByQueryParam(UserQueryParam param);

    SysUser getDetailByIdcard(String idcard);

    /**
     * 根据id查用户信息
     * @param userId
     * @return
     */
    SysUser getUserInfoById(Long  userId);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(SysUser user);
    /**
     *查询用户所属的角色列表
     * @param userId
     */
    List<Long> selectUserRoleList(Long  userId);

    /**
     * 修改用户信息
     * @param param
     * @return
     */
   int updateUser(UserEditParam param);

    /**
     * 根据用户类型获取用户列表
     * @param userType
     * @return
     */
   List<ResultData> getUserListByUserType(@Param("userType") String userType,
                                          @Param("parentId") Integer parentId,
                                          @Param("roleId")Integer roleId,
                                          @Param("userId") Long userId);
}
