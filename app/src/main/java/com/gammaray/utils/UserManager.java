package com.gammaray.utils;

import android.content.Intent;
import android.text.TextUtils;

import com.blankj.utilcode.util.CacheDoubleStaticUtils;
import com.gammaray.MainActivity;
import com.gammaray.base.ActivityManager;
import com.gammaray.base.YunApplication;
import com.gammaray.constant.ExtraConstant;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.EventBusMessageEvent;
import com.gammaray.entity.UserVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.ui.activity.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Synopsis     com.miner.client.utils.UserManager
 * Author       mosr
 * Version      1.0.0
 * Create       2020.6.23 13:28
 * Email        intimatestranger@sina.cn
 */
public class UserManager {
    private static UserVo mUserVo;

    public static void setmUserVo(UserVo mUserVo) {
        setmUserVo(mUserVo, true);
    }

    public static void setmUserVo(UserVo mUserVo, boolean notify) {
        setmUserVo(mUserVo, notify, true);
    }

    public static void setmUserVo(UserVo mUserVo, boolean notify, boolean bSkipLogin) {
        UserManager.mUserVo = mUserVo;
        if (null == mUserVo) {
            CacheDoubleStaticUtils.remove(ExtraConstant.CACHE_LOGIN_INFO);
            getmUserVo(bSkipLogin);
        } else {
            CacheDoubleStaticUtils.put(ExtraConstant.CACHE_LOGIN_INFO, mUserVo);
        }
        if (notify)
            EventBus.getDefault().post(new EventBusMessageEvent(ExtraConstant.EVENT_REFRESH_USER, null));
    }


    public static UserVo getmUserVo() {
        return getmUserVo(true);
    }

    public static UserVo getmUserVo(boolean bSkipLogin) {
        if (!isLogin()) {
            if (bSkipLogin) {
                skipLogin();
            }
        }
        if (null == mUserVo) {
//            mUserVo = new UserVo();
        }
        return mUserVo;
    }

    public static boolean isLogin() {
        return isLogin(false);
    }

    public static boolean isLogin(boolean bSkipLogin) {
        if (null == mUserVo) {
            Object CACHE_LOGIN_INFO = CacheDoubleStaticUtils.getSerializable(ExtraConstant.CACHE_LOGIN_INFO);
            mUserVo = CACHE_LOGIN_INFO instanceof UserVo ? (UserVo) CACHE_LOGIN_INFO : null;
        }
        boolean bLoginSuccess = null != UserManager.mUserVo && !TextUtils.isEmpty(UserManager.mUserVo.getToken());
        if (!bLoginSuccess && bSkipLogin)
            skipLogin();
        return bLoginSuccess;
    }

    public static void logout() {
        logout(true);
    }

    public static void logout(boolean bSkipLogin) {
        UserManager.mUserVo = null;
        CacheDoubleStaticUtils.remove(ExtraConstant.CACHE_LOGIN_INFO);
        if (bSkipLogin) {
            skipLogin();
        }
    }

    private static void skipLogin() {
        if (ActivityManager.getAppManager().isOpenActivity(LoginActivity.class)) {
            ActivityManager.getAppManager().returnToActivity(LoginActivity.class);
        } else {
            ActivityManager.getAppManager().finishExcludeActivity(MainActivity.class, LoginActivity.class);
            Intent mIntent = new Intent(YunApplication.getInstance(), LoginActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            YunApplication.getInstance().startActivity(mIntent);
        }
    }

    public static void refreshUser(final boolean notify) {
        if (isLogin())
            RequestManager.instance().queryUser(new MinerCallback<BaseResponseVo<UserVo>>() {
                @Override
                public void onSuccess(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                    setmUserVo(response.body().getBody(), notify);
                }

                @Override
                public void onError(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {

                }

                @Override
                public void onFailure(Call<?> call, Throwable t) {

                }
            });
    }
}
