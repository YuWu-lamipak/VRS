package com.lemei.common.utils.sign;


import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * AES 工具类
 */

public class AESUtil {
    private static Logger log = LoggerFactory.getLogger(AESUtil.class);
    /**
     * 128位的AESkey
     */

    private static final String AES_KEY_STR= "0123456789wsz131";
    private static final byte[] AES_KEY = AES_KEY_STR.getBytes(StandardCharsets.UTF_8);

    /**
     * AES解密
     *
     * @param data 待解密内容
     * @return 字节数组
     */
    public static byte[] decrypt(byte[] data) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = getCipher(AES_KEY, Cipher.DECRYPT_MODE);
        return cipher.doFinal(data);
    }

    /**
     * AES 加密操作
     *
     * @param data 待加密内容
     * @return 字节数组
     */
    public static byte[] encrypt(byte[] data) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = getCipher(AES_KEY, Cipher.ENCRYPT_MODE);
        return cipher.doFinal(data);
    }


    /**
     * AES 加密操作
     *
     * @param text 待加密内容
     * @return Base64转码后的加密数据
     */
    public static String encrypt(String text) {
        byte[] byteContent = text.getBytes(StandardCharsets.UTF_8);
        try {
            byte[] result = encrypt(byteContent);// 加密
            return org.apache.commons.codec.binary.Base64.encodeBase64String(result);//通过Base64转码返回
        } catch (Exception e) {
            log.info("Error message: {}", e.getMessage());
        }
        return null;
    }

    /**
     * AES 解密操作
     *
     * @param text
     * @return
     */
    public static String decrypt(String text) {
        byte[] bytes = Base64.decodeBase64(text);
        try {
            byte[] result = decrypt(bytes);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.info("Error message: {}", e.getMessage());
        }

        return null;
    }


    /**
     * 获取加密器
     * @param key
     * @param model
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */
    private static Cipher getCipher(byte[] key, int model)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(model, secretKeySpec);
        return cipher;
    }
}
