package com.yunhualian.net;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//加密工具类
public class EncryptUtils {

    public static final String SALT = "#EDI*o_s@id";//接口数据安全加密key 盐

    /**
     * @return MD5加密后的字符串
     * @Des 使用盐 得到相应的一个MD5加密后的字符串
     */
    public static String signature(String str) {
        try {
            ;
            byte[] bs;
            StringBuffer stingBuffer = new StringBuffer();
            // 1.指定加密算法
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 2.将需要加密的字符串转化成byte类型的数据，然后进行哈希过程
            if (TextUtils.isEmpty(str)) {
                bs = digest.digest((SALT).getBytes());
            } else {
                bs = digest.digest((SALT + str).getBytes());
            }
            // 3.遍历bs,让其生成32位字符串，固定写法

            // 4.拼接字符串
            for (byte b : bs) {
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                stingBuffer.append(hexString);
            }
            return stingBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @return MD5加密后的字符串
     * @Des 得到相应的一个MD5加密后的字符串
     */
    public static String encoder(String str) {
        try {
            StringBuffer stingBuffer = new StringBuffer();
            // 1.指定加密算法
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 2.将需要加密的字符串转化成byte类型的数据，然后进行哈希过程
            byte[] bs = digest.digest((str).getBytes());
            // 3.遍历bs,让其生成32位字符串，固定写法

            // 4.拼接字符串
            for (byte b : bs) {
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                stingBuffer.append(hexString);
            }
            return stingBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


}
