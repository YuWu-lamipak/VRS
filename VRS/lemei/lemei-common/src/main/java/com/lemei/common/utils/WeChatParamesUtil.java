package com.lemei.common.utils;

/**
 * @author yq
 * @CLassName WeChatParamesUtil
 * @Description 企业微信参数
 * @date 2022/11/9 9:09
 **/

public class WeChatParamesUtil {
    /**
     * 企业微信配置参数
     * 
     * 配置方式：
     * 1. 通过环境变量配置（推荐）
     *    - WECHAT_CORP_ID: 企业ID
     *    - WECHAT_CORP_SECRET: 企业应用密钥
     *    - WECHAT_AGENT_ID: 企业应用ID
     * 
     * 2. 通过 application.yml 配置
     *    wechat:
     *      corpId: your-corp-id
     *      corpSecret: your-corp-secret
     *      agentId: 1000002
     * 
     * 获取方式：
     * 1. 登录企业微信管理后台
     * 2. 进入"应用管理"查看应用详情
     * 3. 在"我的企业"中查看企业ID
     */
    
    /** 企业ID */
    public final static String corpId = getEnvOrDefault("WECHAT_CORP_ID", "");
    
    /** 企业应用密钥 */
    public final static String corpsecret = getEnvOrDefault("WECHAT_CORP_SECRET", "");
    
    /** 企业应用ID */
    public final static int agentId = Integer.parseInt(getEnvOrDefault("WECHAT_AGENT_ID", "1000002"));
    
    /**
     * 获取环境变量，如果不存在则返回默认值
     */
    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
}