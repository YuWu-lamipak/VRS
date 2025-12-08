package com.lemei.common.utils;

/**
 * 处理 记录日记文件
 */
public class LogUtils {
    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
