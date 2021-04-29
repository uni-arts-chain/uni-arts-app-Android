package com.yunhualian.base;

import android.app.Application;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.igexin.sdk.PushManager;
import com.yunhualian.constant.ExtraConstant;
import com.yunhualian.entity.ArtMaterialVo;
import com.yunhualian.entity.ArtPriceVo;
import com.yunhualian.entity.ArtThemeVo;
import com.yunhualian.entity.ArtTypeVo;
import com.yunhualian.entity.EventBusMessageEvent;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.HeaderIntercepter;
import com.yunhualian.net.LogInterceptor;
import com.yunhualian.net.NetworkManager;
import com.yunhualian.service.TokenInterceptor;
import com.yunhualian.utils.UserManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.co.soramitsu.app.App;
import okhttp3.OkHttpClient;


public class YunApplication extends App {
    private static YunApplication mYunApplicaion;
    public static boolean isLaunch = true;
    public static List<ArtTypeVo> artTypelist;
    public static List<ArtTypeVo> artThemeVoList;
    public static List<ArtPriceVo> artPriceVoList;
    public static String PAY_CURRENCY = "UART";
    private static DemoHandler handler;
    private static String Token;
    private int DEFAULT_TIMEOUT = 20000;
    public static String LIVE2D_CACHE_PATH;
    public static final String MODEL_PATH = ".model3.json";

    public static String getToken() {
        return Token;
    }

    public static void setToken(String token) {
        Token = token;
    }

    public static List<ArtPriceVo> getArtPriceVoList() {
        return artPriceVoList;
    }

    public static void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    public static String isCIDOnLine = "";
    public static String cid = "";

    public static void setArtPriceVoList(List<ArtPriceVo> artPriceVoList) {
        YunApplication.artPriceVoList = artPriceVoList;
    }

    public static StringBuilder payloadData = new StringBuilder();

    public static List<ArtTypeVo> getArtTypelist() {
        return artTypelist;
    }


    public static List<ArtTypeVo> getArtThemeVoList() {
        return artThemeVoList;
    }

    public static void setArtThemeVoList(List<ArtTypeVo> artThemeVoList) {
        YunApplication.artThemeVoList = artThemeVoList;
    }


    public static void setArtTypelist(List<ArtTypeVo> artTypelist) {
        YunApplication.artTypelist = artTypelist;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mYunApplicaion = this;
        ARouter.openDebug();
        ARouter.init(this);
        PushManager.getInstance().initialize(this);
        NetworkManager.instance().init();
        initOkhttpUtils();
        if (handler == null) {
            handler = new DemoHandler();
        }
        LIVE2D_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator.concat("Yunhualian/live2d/");
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
        return getmUserVo(false);
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

    public static class DemoHandler extends Handler {
        public static final int RECEIVE_MESSAGE_DATA = 0; //接收到消息
        public static final int RECEIVE_CLIENT_ID = 1; //cid通知
        public static final int RECEIVE_ONLINE_STATE = 2; //cid在线状态通知消息
        public static final int SHOW_TOAST = 3; //Toast消息


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECEIVE_MESSAGE_DATA:  //接收到消息
                    payloadData.append((String) msg.obj);
                    payloadData.append("\n");
                    LogUtils.e("RECEIVE_MESSAGE_DATA" + payloadData.toString());
//                    if (demoActivity != null && demoActivity.get() != null) {
//                        if (demoActivity.get().homeFragment != null && demoActivity.get().homeFragment.tvLog != null) {
//                            demoActivity.get().homeFragment.tvLog.append(msg.obj + "\n");
//                        }
//                    }
                    EventBus.getDefault().post(new EventBusMessageEvent(ExtraConstant.EVENT_PUSH, msg.obj));
                    break;

                case RECEIVE_CLIENT_ID:  //cid通知
                    LogUtils.e("RECEIVE_CLIENT_ID");
                    cid = (String) msg.obj;
//                    if (demoActivity != null && demoActivity.get() != null) {
//                        if (demoActivity.get().homeFragment != null && demoActivity.get().homeFragment.tvClientId != null) {
//                            demoActivity.get().homeFragment.tvClientId.setText((String) msg.obj);
//                        }
//                    }
                    break;
                case RECEIVE_ONLINE_STATE:  //cid在线状态通知
                    isCIDOnLine = (String) msg.obj;
//                    if (demoActivity != null && demoActivity.get() != null) {
//                        if (demoActivity.get().homeFragment != null && demoActivity.get().homeFragment.tvCidState != null) {
//                            demoActivity.get().homeFragment.tvCidState.setText(msg.obj.equals("true") ? demoActivity.get().getResources().getString(R.string.online) : demoActivity.get().getResources().getString(R.string.offline));
//                        }
//                    }
                    break;
                case SHOW_TOAST:  //需要弹出Toast

                    LogUtils.e("SHOW_TOAST");
                    Toast.makeText(YunApplication.getInstance(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }

        }

    }

    public void initOkhttpUtils() {
        try {
            AssetManager assetManager = getAssets();
            InputStream[] inputStreams = new InputStream[1];
            inputStreams[0] = assetManager.open("3736864_www.stockcb.com.pem");
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(inputStreams, null, null);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(false)
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .addInterceptor(new TokenInterceptor(this))
                    .addInterceptor(new LogInterceptor())
                    .addInterceptor(new HeaderIntercepter())
                    .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                    //其他配置
                    .build();
            OkHttpUtils.initClient(okHttpClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
