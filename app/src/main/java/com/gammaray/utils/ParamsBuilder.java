package com.gammaray.utils;

import java.util.HashMap;
import java.util.Set;

import okhttp3.Request;

public class ParamsBuilder {
    /**
     * 取得参数组合String
     *
     * @param map 参数hashMap
     * @return
     */
    public static String getParamsGroup(HashMap<String, String> map) {
        StringBuilder builder = new StringBuilder();
        builder.append("|");

        int i = 0;

        Set<String> keySet = map.keySet();

        for (String key : keySet) {
            i++;

            if (i < map.size())
                builder.append(key + "=" + map.get(key) + "&");
            else
                builder.append(key + "=" + map.get(key));
        }

        return builder.toString();
    }

    public static String getRequestGroup(Request _request) {
        StringBuilder request = new StringBuilder();
        request.append(StringUtils.formatUpperCase(_request.method())).
                append("|").
                append(_request.url().encodedPath());

        return request.toString();
    }


    public static String getSign(Request request, HashMap<String, String> map) {
        StringBuilder sign = new StringBuilder(getRequestGroup(request));
        sign.append(getParamsGroup(map));

        return sign.toString();
    }

}