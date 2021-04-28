package com.yunhualian.utils;

import android.text.TextUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by gengda on 2018/3/30.
 */

public class MD5Encrypt {

    public static String HMACSHA256(String data, String key) {
        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(key))
            return "";

        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            return byte2hex(mac.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString();
    }

    /**
     * 按数字字母升序
     *
     * @param hashMap
     * @return
     */
    public static HashMap orderByASC(HashMap<String, String> hashMap) {
        LinkedHashMap<String, String> order = new LinkedHashMap<>();

        if (null == hashMap)
            return order;

        Set<String> set = hashMap.keySet();

        if (null == set)
            return order;

        Object[] keys = set.toArray();
        Arrays.sort(keys);

        for (int i = 0; i < keys.length; i++) {
            order.put((String) keys[i], hashMap.get(keys[i]));
        }

        return order;
    }

}
