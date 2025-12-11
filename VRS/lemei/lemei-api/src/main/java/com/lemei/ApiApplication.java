package com.lemei;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;


/**
 * WEB 端Api 接口
 */
@SpringBootApplication(exclude = { 
    DataSourceAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
@EnableScheduling
@EnableAsync //开启异步注解
public class ApiApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ApiApplication.class, args);
        try{
            Environment env = applicationContext.getEnvironment();
            String port = env.getProperty("server.port");
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Swagger文档: \thttp://" + ip+":"+port+"/swagger-ui/index.html#/");
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("前台程序启动完成！");
    }
}
