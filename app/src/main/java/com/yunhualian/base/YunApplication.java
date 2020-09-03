package com.yunhualian.base;

import android.app.Application;

import com.yunhualian.entity.UserVo;
import com.yunhualian.utils.UserManager;

public class YunApplication extends Application {
    private static YunApplication mYunApplicaion;

    @Override
    public void onCreate() {
        super.onCreate();
        mYunApplicaion = this;
    }
    public static void setmUserVo(UserVo mUserVo) {
        UserManager.setmUserVo(mUserVo);
    }

    public static void setmUserVo(UserVo mUserVo, boolean notify) {
        UserManager.setmUserVo(mUserVo, notify);
    }

    public static void setmUserVo(UserVo mUserVo, boolean notify, boolean bSkipLogin) {
        UserManager.setmUserVo(mUserVo, notify, bSkipLogin);
    }

    public static UserVo getmUserVo() {
        return getmUserVo(true);
    }

    public static UserVo getmUserVo(boolean bSkipLogin) {
        return UserManager.getmUserVo(bSkipLogin);
    }

    public static YunApplication getInstance() {
        return mYunApplicaion;
    }
    public static boolean isLogin() {
        return isLogin(false);
    }

    public static boolean isLogin(boolean bSkipLogin) {
        return UserManager.isLogin(bSkipLogin);
    }

    public static void refreshUser(boolean notify) {
        UserManager.refreshUser(notify);
    }

    public static void logout() {
        UserManager.logout();
    }

    public static void logout(boolean bSkipLogin) {
        UserManager.logout(bSkipLogin);
    }

}