package com.lemei.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 外部服务配置
 * 
 * 用于管理 ESB、SAP 等外部服务的 URL 配置
 * 
 * 配置方式：
 * 1. 在 application.yml 中配置
 * 2. 通过环境变量配置
 * 
 * @author system
 * @date 2025-12-11
 */
@Configuration
public class ExternalServiceConfig {
    
    /**
     * ESB 服务地址
     * 配置示例：https://your-esb-server:8020/esb/comm/service
     */
    @Value("${esb.service.url:https://172.18.165.151:8020/esb/comm/service}")
    private String esbServiceUrl;
    
    /**
     * SAP 服务地址
     * 配置示例：https://your-sap-server:9020/sap/SD024/third/noparams
     */
    @Value("${sap.service.url:https://esbapi.lamipak.biz:9020/sap/SD024/third/noparams}")
    private String sapServiceUrl;
    
    public String getEsbServiceUrl() {
        return esbServiceUrl;
    }
    
    public String getSapServiceUrl() {
        return sapServiceUrl;
    }
}
