package com.gammaray.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashMap;
import java.util.Map;


/**
 * 手机信息获取
 *
 * @author yinsh
 */
public class PhoneInfo {




    /**
     * 检查网络状态
     *
     * @return false 为没有可用网络 true 为当前网络正常
     */
    public static boolean hasInternet(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }
        return true;
    }


    /**
     * @return
     * @Description 所有接口请求需要的系统信息
     * @author wy
     * @date 2015-4-2 下午5:50:15
     */
    public static Map<String, Object> getSystemInfo() {
        Map<String, Object> params = new HashMap<String, Object>();
        try {
//            if (DDApplication.getInstance().getUser() != null && !TextUtils.isEmpty(DDApplication.getInstance().getUser().getUser_id())) {
//                params.put("user_id", DDApplication.getInstance().getUser().getUser_id());
//            }
//            if (DDApplication.getInstance().getUser() != null && !TextUtils.isEmpty(DDApplication.getInstance().getUser().getToken())) {
//                params.put("token", DDApplication.getInstance().getUser().getToken());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }



}
