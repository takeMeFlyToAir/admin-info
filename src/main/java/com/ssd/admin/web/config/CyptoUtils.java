package com.ssd.admin.web.config;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 加密工具类
 */
public class CyptoUtils {


    /**
     * Md5加密，默认使用shiro提供的Md5方式
     * @param source 需要加密的字符串
     * @return 加密后的字符串
     */
    public static String md5(String source) {
        String md5 = new Md5Hash(source).toString();
        return md5;
    }

}
