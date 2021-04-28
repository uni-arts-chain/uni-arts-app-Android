package com.yunhualian.utils;

import android.content.Context;

import com.lljjcoder.style.citylist.Toast.ToastUtils;


/**
 * @author wy
 * @Description
 * @date 2015-5-5 上午11:18:35
 */
public class ErrorHelper {

    public static final int NoNetwork = -1;//网络连接失败
    public static final int CANCELL = 0;//用户取消
    public static final int NetWork_Slow = 1;//网络加载缓慢

    /**
     * Returns appropriate message which is to be displayed to the user against the specified error object.
     *
     * @param error
     * @param message
     * @param context @return
     */
    public static void getMessage(int error, String message, Context context) {
        try {
            if (context != null) {
                if (error == NoNetwork) {
                    if (PhoneInfo.hasInternet(context)) {
                        ToastUtils.showLongToast(context, "请检查网络连接！");
                    } else {
                        ToastUtils.showLongToast(context, "网络连接失败");
                    }
                } else if (error == CANCELL) {
                } else if (error == NetWork_Slow) {
                    if (message.equals("404") || message.equals("422") || message.equals("500")) {
                        ToastUtils.showLongToast(context, "服务器异常");
                    } else if (message.equals("401")) {
                        ToastUtils.showLongToast(context, "登录失效,请重新登录！");
                    } else {
                        ToastUtils.showLongToast(context, "请检查网络连接！");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
