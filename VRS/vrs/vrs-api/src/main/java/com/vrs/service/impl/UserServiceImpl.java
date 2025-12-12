package com.vrs.service.impl;

import com.vrs.common.annotation.DataScope;
import com.vrs.common.annotation.Log;
import com.vrs.common.constant.Constants;
import com.vrs.common.core.domain.AjaxResult;
import com.vrs.common.core.domain.ResultData;
import com.vrs.common.core.domain.entity.SysUser;
import com.vrs.common.core.domain.model.LoginUser;
import com.vrs.common.core.redis.RedisCache;
import com.vrs.common.utils.*;
import com.vrs.common.utils.ip.IpUtils;
import com.vrs.domain.param.UserEditParam;
import com.vrs.domain.param.UserQueryParam;
import com.vrs.framework.manager.AsyncManager;
import com.vrs.framework.manager.factory.AsyncFactory;
import com.vrs.framework.web.service.TokenService;
import com.vrs.mapper.UserMapper;
import com.vrs.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;


/**
 * @author yq  用户Service
 * @CLassName UserService
 * @Description
 * @date 2022/8/26 15:16
 **/
@Component
public class UserServiceImpl  implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TokenService tokenService;

    @Value("${token.soloLogin}")
    private boolean soloLogin;

    @Autowired
    private RedisCache redisCache;
    /**
     *用户登录
     * @param phone
     * @param password
     * @return
     */
    @Override
    public Object userLogin( String phone,String password){
        // 用户验证
        SysUser sysUser =userMapper.getUserByPhone(phone);
//        if(phone.equals("admin")) return AjaxResult.error("账号不存在!");
        if (sysUser==null){
            sysUser = userMapper.getUserByUserName(phone);
        }
        if (sysUser==null ){ return AjaxResult.error("账号不存在！");}
        if ("2".equals(sysUser.getDelFlag()) ){ return AjaxResult.error("账号不存在!");}
        if ("1".equals(sysUser.getStatus()) ){ return AjaxResult.error("账号已禁用,请联系管理员!");}
        if (!SecurityUtils.matchesPassword(password,sysUser.getPassword()))
        {return AjaxResult.error("密码不正确!");}
        //异步添加日记
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(sysUser.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));

        //增加登录日记到
        recordLoginInfo(sysUser.getUserId());

        //不允许多端登录的情况下,清除缓存信息
        if (!soloLogin){
            String userIDKey =Constants.LOGIN_USERID_KEY +sysUser.getUserId();
            String userKey = redisCache.getCacheObject(userIDKey);
            logger.info(userIDKey,userKey);
            if (StringUtils.isNotEmpty(userKey)){
                redisCache.deleteObject(userIDKey);
                redisCache.deleteObject(userKey);
            }
        }

        //用户拥有的角色信息
        List<Long> roleList = userMapper.selectUserRoleList(sysUser.getUserId());
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(sysUser);
        loginUser.setUserId(sysUser.getUserId());

        String token= tokenService.createToken(loginUser);
        HashMap<String,Object> map = new HashMap<>();
        map.put("token",token);
        map.put("userType",sysUser.getUserType());
        map.put("roleList",roleList);
        return AjaxResult.success(map);
    }


    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userMapper.updateUser(sysUser);
    }

    /**
     * 获取用户信息
     * @return
     */
    @Override
    public SysUser getUserInfo(){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        UserQueryParam param = new UserQueryParam();
        param.setUserId(loginUser.getUserId());
        return userMapper.selectUserByQueryParam(param);
    }

    @Override
    public SysUser getUserInfoById(Long userId){

        return userMapper.getUserInfoById(userId);
    }



    @Override
    public SysUser getDetailByIdcard(String idcard)
    {
        return userMapper.getDetailByIdcard(idcard);
    }

    /**
     * 修改用户信息
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserInfo(UserEditParam param){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        param.setUserId(loginUser.getUserId());
        param.setUpdateBy(loginUser.getUsername());
        return userMapper.updateUser(param);

    }

    /**
     * 获取不同用户类型的用户
     * @param userType
     * @return
     */
    @Override
    public List<ResultData> getUsersByUserType(String userType,Integer parentId,Integer roleId,Long userId){
        return userMapper.getUserListByUserType(userType,parentId,roleId,userId);

    }
}
