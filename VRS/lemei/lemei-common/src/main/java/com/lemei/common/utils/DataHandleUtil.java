package com.lemei.common.utils;

import java.util.Random;

/**
 * 数据类型处理方法
 * @author yq
 * @CLassName DataHandleUtil
 * @Description
 * @date 2022/8/25 16:36
 **/
public class DataHandleUtil {

    /**
     * 获取指定长度的 随机数
     * @param prefix
     * @param length
     * @return
     */
    public static String getId_random(String prefix, Integer length) {
        StringBuilder str=new StringBuilder();//定义变长字符串
        Random random=new Random();
        str.append(prefix != null ? prefix : "");
        for(int i=0;i<length;i++){
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
