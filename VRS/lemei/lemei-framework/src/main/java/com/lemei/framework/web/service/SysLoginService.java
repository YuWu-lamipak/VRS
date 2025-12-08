package com.lemei.framework.web.service;


import com.lemei.common.constant.Constants;
import com.lemei.common.core.domain.ArticleResponseData;
import com.lemei.common.core.domain.entity.SysUser;
import com.lemei.common.core.domain.model.LoginUser;
import com.lemei.common.core.redis.RedisCache;
import com.lemei.common.exception.ServiceException;
import com.lemei.common.exception.user.CaptchaException;
import com.lemei.common.exception.user.CaptchaExpireException;
import com.lemei.common.exception.user.UserPasswordNotMatchException;
import com.lemei.common.utils.*;
import com.lemei.common.utils.ip.IpUtils;
import com.lemei.framework.manager.AsyncManager;
import com.lemei.framework.manager.factory.AsyncFactory;
import com.lemei.system.service.ISysConfigService;
import com.lemei.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录校验 方法
 */
@Component
public class SysLoginService {
    private final Logger logger = LoggerFactory.getLogger(SysLoginService.class);
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Value("${token.soloLogin}")
    private boolean soloLogin;


    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid)
    {
        boolean captchaOnOff = configService.selectCaptchaOnOff();
        // 验证码开关
        if (captchaOnOff)
        {
            validateCaptcha(username, code, uuid);
        }
        // 用户验证
        Authentication authentication = null;
        try
        {
            // 该方法会去调用 UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        if("02".equals(loginUser.getUser().getUserType())){
            throw new ServiceException("您不能登录后台系统");
        }
        //不允许多端登录的情况下,清楚缓存信息
        if (!soloLogin){
            String userIDKey =Constants.LOGIN_USERID_KEY +loginUser.getUser().getUserId();
            String userKey = redisCache.getCacheObject(userIDKey);
            logger.info(userIDKey,userKey);
            if (StringUtils.isNotEmpty(userKey)){
                redisCache.deleteObject(userIDKey);
                redisCache.deleteObject(userKey);
            }
        }
        String token = tokenService.createToken(loginUser);
        // 生成token
        return token;
    }

    /**
     *用户登录
     * @param phone
     * @param password
     * @return
     */
    public Object UserLogin( String phone,String password){
        // 用户验证
        SysUser sysUser =userService.selectUserPhone(phone);
        if (sysUser==null){
            sysUser = userService.selectUserByUserName(phone);
        }

        if (sysUser==null ) return ArticleResponseData.fail("账号或者密码不正确!");
        if (sysUser.getDelFlag().equals("2") ) return ArticleResponseData.fail("账号不存在!");
        if (sysUser.getStatus().equals("1") ) return ArticleResponseData.fail("账号已禁用,请联系管理员!");
        //SecurityUtils.encryptPassword()
        if (!SecurityUtils.matchesPassword(password,sysUser.getPassword()))return ArticleResponseData.fail("密码不正确!");
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(sysUser.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        //LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //recordLoginInfo(loginUser.getUserId());

        //增加登录日记到
        recordLoginInfo(sysUser.getUserId());


        //不允许多端登录的情况下,清楚缓存信息
        if (!soloLogin){
            String userIDKey =Constants.LOGIN_USERID_KEY +sysUser.getUserId();
            String userKey = redisCache.getCacheObject(userIDKey);
            logger.info(userIDKey,userKey);
            if (StringUtils.isNotEmpty(userKey)){
                redisCache.deleteObject(userIDKey);
                redisCache.deleteObject(userKey);
            }
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(sysUser);
        String token= tokenService.createToken(loginUser);
        return ArticleResponseData.ok(token,200);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
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
        userService.updateUserProfile(sysUser);
    }
}
