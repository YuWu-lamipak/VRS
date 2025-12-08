package com.lemei.common.annotation;


import java.lang.annotation.*;

/**
 * 自定义加解密注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface EncryptFilter {

    /**
     * 对入参是否解密
     *
     * @return
     */
    boolean decryptRequest() default true;

    /**
     * 对出参是否加密
     */
    boolean encryptResponse() default true;

}
