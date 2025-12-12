package com.vrs.framework.web.service;


import com.vrs.common.constant.Constants;
import com.vrs.common.constant.UserConstants;
import com.vrs.common.core.domain.ArticleResponseData;
import com.vrs.common.core.domain.entity.SysUser;
import com.vrs.common.core.domain.model.RegisterBody;
import com.vrs.common.core.domain.model.RegisterUserBody;
import com.vrs.common.core.redis.RedisCache;
import com.vrs.common.exception.user.CaptchaException;
import com.vrs.common.exception.user.CaptchaExpireException;
import com.vrs.common.utils.MessageUtils;
import com.vrs.common.utils.SecurityUtils;
import com.vrs.common.utils.StringUtils;
import com.vrs.framework.manager.AsyncManager;
import com.vrs.framework.manager.factory.AsyncFactory;
import com.vrs.system.mapper.SysUserMapper;
import com.vrs.system.service.ISysConfigService;
import com.vrs.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * 注册 校验 方法
 */
@Component
public class SysRegisterService {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysConfigService configService;


    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate redisTemplate;
    private final String Code_key = "yzm";

    @Value("${Sms.time}")
    private long SmsTime;

    /**
     * 用户注册
     * @param registerUserBody
     * @return
     */
    public Object   UserRegister(RegisterUserBody registerUserBody){
        //boolean captcha = configService.SelectUserCaptchaOnOff();

        //if (registerUserBody.getUsername().equals("")|| registerUserBody.getUsername()==null)return ArticleResponseData.fail("用户名不能为空!");
        if (registerUserBody.getPassword().equals("")||registerUserBody.getPassword()==null) return ArticleResponseData.fail("密码不能为空!");
        if (registerUserBody.getImgUrl()==null) return ArticleResponseData.fail("头像地址不能为空!");
//        if (captcha){
//            validateCaptcha(registerUserBody.getPhone(),registerUserBody.getCode(),registerUserBody.getUuid());
//        }
        SysUser sysUser = new SysUser();
        sysUser.setPhoneNumber(registerUserBody.getPhone());
        if ("1".equals(userService.checkPhoneUnique(sysUser)))
            return ArticleResponseData.fail("注册失败,手机号码已存在!");
        if ("1".equals(userService.checkUserNameUnique(registerUserBody.getUsername())))
            return ArticleResponseData.fail("注册失败,用户名已存在!");
        //Object o = redisTemplate.opsForValue().get("code_"+Code_key+registerUserBody.getPhone());
        //if (o==null) return ArticleResponseData.fail("验证码已失效!,请重新获取");
        //String code= (String) o;
        //if (!code.equals(registerUserBody.getCode()))return ArticleResponseData.fail("验证码错误,请重试!");
        if(!registerUserBody.getCode().equals("123456")) return ArticleResponseData.fail("验证码错误,请重试");
        sysUser.setUserName(registerUserBody.getUsername());
        sysUser.setNickName(registerUserBody.getNickname());
        sysUser.setPassword(SecurityUtils.encryptPassword(registerUserBody.getPassword()));
        sysUser.setAvatar(registerUserBody.getImgUrl());
        boolean regFlag = userService.registerUser(sysUser);
        if (!regFlag)
        {
            return ArticleResponseData.fail("注册失败,请联系管理员!");
        }
        else
        {
            if(StringUtils.isEmpty(registerUserBody.getPersonal_profile())){
                registerUserBody.setPersonal_profile("这个人很懒,没有简介");
            }
            SysUser sysUser1= userService.selectUserPhone(registerUserBody.getPhone());
            //sysUserMapper.UpdateUserPer(registerUserBody.getPersonal_profile(),sysUser1.getPhonenumber());
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(registerUserBody.getUsername(), Constants.REGISTER,
                    MessageUtils.message("user.register.success")));
        }
        return ArticleResponseData.ok("注册成功!",200);
    }

    /**
     * 发送验证码可能需要验证码接口
     * @param registerUserBody
     * @return
     */
    public Object UserSms(RegisterUserBody registerUserBody){
        //验证码接口
        boolean captcha = configService.SelectUserCaptchaOnOff();
        if (captcha){
            validateCaptcha(registerUserBody.getPhone(),registerUserBody.getCode(),registerUserBody.getUuid());
        }
        String code= this.getRandomCode(6);
        SysUser sysUser = new SysUser();
        sysUser.setPhoneNumber(registerUserBody.getPhone());
        if ("1".equals(userService.checkPhoneUnique(sysUser)))
            return ArticleResponseData.fail("发送验证码失败,手机号码已存在!");
        Object o = redisTemplate.opsForValue().get("code_"+Code_key+registerUserBody.getPhone());
        if (o!=null){
            long  time = redisTemplate.opsForValue().getOperations().getExpire("code_"+Code_key+registerUserBody.getPhone());
            if (SmsTime - time >0){
                time =SmsTime-time;
                return ArticleResponseData.fail("请在"+ (SmsTime-time)+"秒后再试");
            }
        }
        updateCode("code_"+Code_key+registerUserBody.getPhone(),code);
        return ArticleResponseData.ok("发送验证码成功!",200);
    }

    /**
     * 存储到redis缓存中
     * @param key
     * @param value
     */
    private void updateCode(String key,String value){
        Object o  =redisTemplate.opsForValue().get(key);
        if (o!=null){
            if (redisTemplate.delete(key)){
                redisTemplate.opsForValue().set(key,value,300, TimeUnit.SECONDS);
            }
        }else {
            redisTemplate.opsForValue().set(key,value,300,TimeUnit.SECONDS);
        }
    }

    /**
     * 生成6位验证码
     * @param code
     * @return
     */
    private   String getRandomCode(Integer code){
        Random random = new Random();
        StringBuffer result= new StringBuffer();
        for (int i=0;i<code;i++){
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

    /**
     * 注册
     */
    public String register(RegisterBody registerBody)
    {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();

        boolean captchaOnOff = configService.selectCaptchaOnOff();
        // 验证码开关
        if (captchaOnOff)
        {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username))
        {
            msg = "用户名不能为空";
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = "用户密码不能为空";
        }
        else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = "账户长度必须在2到20个字符之间";
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "密码长度必须在5到20个字符之间";
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(username)))
        {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        }
        else
        {
            SysUser sysUser = new SysUser();
            sysUser.setUserName(username);
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(registerBody.getPassword()));
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag)
            {
                msg = "注册失败,请联系系统管理人员";
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER,
                        MessageUtils.message("user.register.success")));
            }
        }
        return msg;
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
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }
}
