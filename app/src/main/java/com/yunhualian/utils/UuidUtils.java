package com.yunhualian.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;

import java.util.Random;

public class UuidUtils {
    /**
     * @功能说明： 获取随机字符串32位
     * @author: zwx
     */
    public static String getuuid() {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 32; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        SPUtils.getInstance().put("UUID", sb.toString());
        return sb.toString();
    }

    /**
     * @author yinsh
     * @description 获取唯一UUID
     * @date 2019-10-27 12:41
     */
    public static String getUnqueUUid() {
        String uuid = "";
        String saveuuid = SPUtils.getInstance().getString("UUID", "");
        if (TextUtils.isEmpty(saveuuid)) {
            uuid = getuuid();
        } else {
            uuid = saveuuid;
        }
        return uuid;
    }
}
