package com.yunhualian.net;

import com.blankj.utilcode.util.LogUtils;
import com.yunhualian.utils.MD5Encrypt;
import com.yunhualian.utils.ParamsBuilder;
import com.yunhualian.utils.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yinsh
 * @Description 数据加密
 * @date 2015-1-9 上午11:33:32
 */
public class HttpTools {
    public static Map Params(Map params) {
//        Map map = PhoneInfo.getSystemInfo();
//        params.putAll(map);
//        params.put("signature", SigInfo(params));
        return params;
    }

    // 计算签名信息
    public static String SigInfo(HashMap<String, String> paramsMap, String current, String url, String metod) {
        StringBuilder result = new StringBuilder();
//        Object[] keys = paramsMap.keySet().toArray();
//        Arrays.sort(keys);

//        for (Object key : keys) {
//            result.append(key);
//            result.append(paramsMap.get(key));
//        }
        paramsMap.put(StringUtils.formatLowerCase("Tonce"), current);
        paramsMap = MD5Encrypt.orderByASC(paramsMap);
        // 参数排序后的字符串
        String paramsSortString = result.toString();

//        String signStr = ParamsBuilder.getSign(request, mParamsWithToken);
        String urlsign = url.substring(22);
        String signStr1 = metod.concat("|").concat(urlsign).concat(ParamsBuilder.getParamsGroup(paramsMap));

        String sha256str = MD5Encrypt.HMACSHA256(signStr1, "2021-04-23 20:36:24");
        // md5计算签名
//        String strTSig = RSAUtil.base64Encrypted(EncryptUtils.signature(paramsSortString));
//        LogUtils.i("info==", strTSig);
        LogUtils.i("info==", "Md5" + "====" + EncryptUtils.signature(paramsSortString) + "=====");
        LogUtils.i("sha256str==", "paramsSortString" + "====" + paramsSortString + "=====");
        return sha256str;
    }
}
