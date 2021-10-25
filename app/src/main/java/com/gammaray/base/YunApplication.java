package com.gammaray.base;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.gammaray.constant.AppConstant;
import com.gammaray.constant.ExtraConstant;
import com.gammaray.entity.ArtPriceVo;
import com.gammaray.entity.ArtTypeVo;
import com.gammaray.entity.EventBusMessageEvent;
import com.gammaray.entity.NetworkInfos;
import com.gammaray.entity.UserVo;
import com.gammaray.eth.domain.DaoMaster;
import com.gammaray.eth.domain.DaoSession;
import com.gammaray.eth.entity.NetworkInfo;
import com.gammaray.eth.repository.RepositoryFactory;
import com.gammaray.eth.repository.SharedPreferenceRepository;
import com.gammaray.eth.util.AppFilePath;
import com.gammaray.net.HeaderIntercepter;
import com.gammaray.net.LogInterceptor;
import com.gammaray.net.NetworkManager;
import com.gammaray.service.TokenInterceptor;
import com.gammaray.utils.UserManager;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import jp.co.soramitsu.app.App;
import okhttp3.OkHttpClient;


public class YunApplication extends App {
    private static YunApplication mYunApplicaion;
    public static boolean isLaunch = true;
    public static List<ArtTypeVo> artTypelist;
    public static List<ArtTypeVo> artThemeVoList;
    public static List<ArtPriceVo> artPriceVoList;
    public static String PAY_CURRENCY = "￥";
    private static DemoHandler handler;
    private static String Token;
    private int DEFAULT_TIMEOUT = 20000;
    public static String LIVE2D_CACHE_PATH;
    public static final String MODEL_PATH = ".model3.json";
    public static String path = "";

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

    private DaoSession daoSession;

    private static OkHttpClient httpClient;

    public static RepositoryFactory repositoryFactory;


    public static SharedPreferenceRepository sp;

    public static String NETWORK_RPC_URL;

    public static int NETWORK_CHAIN_ID;

    public static String NETWORK_API_KEY = "7e2855d5896946cb985af8944713a371";

    public DaoSession getDaoSession() {
        return daoSession;
    }

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

    public static OkHttpClient okHttpClient() {
        return httpClient;
    }

    public static RepositoryFactory repositoryFactory() {
        return repositoryFactory;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mYunApplicaion = this;
        System.loadLibrary("TrustWalletCore");
        ARouter.init(this);
        PushManager.getInstance().initialize(this);
        NetworkManager.instance().init();
        AppFilePath.init(this);
        init();
        Realm.init(this);
        initX5();
        initUMeng();
//        initOkhttpUtils();
        if (handler == null) {
            handler = new DemoHandler();
        }
        LIVE2D_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator.concat("GammaRay/live2d/");
    }

    private void init() {

        sp = SharedPreferenceRepository.init(getApplicationContext());

        httpClient = new OkHttpClient.Builder()
                .addInterceptor(new LogInterceptor())
                .build();

        Gson gson = new Gson();

        repositoryFactory = RepositoryFactory.init(sp, httpClient, gson);

        //创建数据库表
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(this, "wallet", null);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        daoSession = new DaoMaster(db).newSession();
    }

    private void initX5() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub

            }
        };
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
            }

            @Override
            public void onInstallFinish(int i) {
            }

            @Override
            public void onDownloadProgress(int i) {
            }
        });
        QbSdk.initX5Environment(this, cb);
    }

    private void initUMeng() {
        //注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
        //用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
        //UMConfigure.init调用中appkey和channel参数请置为null）。
        UMConfigure.init(this, AppConstant.UMENG_APP_KEY, "Android", UMConfigure.DEVICE_TYPE_PHONE, null);
        //设置组件化的Log开关
        //参数: boolean 默认为false，如需查看LOG设置为true
        UMConfigure.setLogEnabled(true);
        // 选用LEGACY_AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_MANUAL);
        // 支持在子进程中统计自定义事件
        UMConfigure.setProcessEvent(true);
    }

    public static void setNetWorkInfo(List<NetworkInfos> netWorkInfos) {
        UserManager.setNetWorkInfo(netWorkInfos);
    }

    public static List<NetworkInfos> getNetWorkInfos() {
        return UserManager.getNetWorkInfos();
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
                    .addInterceptor(new TokenInterceptor())
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
