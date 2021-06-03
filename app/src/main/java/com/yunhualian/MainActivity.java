package com.yunhualian;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.upbest.arouter.ArouterModelPath;
import com.upbest.arouter.EventEntity;
import com.upbest.arouter.Extras;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.YunApplication;
import com.yunhualian.constant.AppConstant;
import com.yunhualian.constant.ExtraConstant;
import com.yunhualian.databinding.ActivityMainBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.EventBusMessageEvent;
import com.yunhualian.entity.ReceiverPushBean;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.fragment.BlindBoxFragment;
import com.yunhualian.ui.fragment.CreatorFragment;
import com.yunhualian.ui.fragment.HomeFragment;
import com.yunhualian.ui.fragment.MineFragment;
import com.yunhualian.ui.fragment.PictureSortFragment;
import com.yunhualian.utils.NotificationUtil;
import com.yunhualian.utils.SharedPreUtils;
import com.yunhualian.utils.UserManager;
import com.yunhualian.widget.PermissionDialog;
import com.yunhualian.widget.UpdateDialog;

import org.bouncycastle.util.encoders.Hex;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import jp.co.soramitsu.fearless_utils.encrypt.EncryptionType;
import jp.co.soramitsu.fearless_utils.encrypt.SignatureWrapper;
import jp.co.soramitsu.fearless_utils.encrypt.Signer;
import jp.co.soramitsu.fearless_utils.encrypt.model.Keypair;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import retrofit2.Call;
import retrofit2.Response;

@Route(path = ArouterModelPath.MAIN)
public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private HomeFragment homeFragment;
    private PictureSortFragment pictureSortFragment;
    private MineFragment mineFragment;
    private CreatorFragment creatorFragment;
    private BlindBoxFragment blindBoxFragment;
    private boolean mIsback;
    private Fragment mCurrentFragment;
    private int mCurrentItemId;
    private List<String> mLackedPermission = new ArrayList<>();
    private BottomNavigationView mBottomNavigationView;
    public UpdateDialog mDialog;
    public UpdateDialog mForceDialog;
    public static final String JUMP_PAGE = "jump_page";


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        if (!PermissionUtils.isGranted(PermissionConstants.getPermissions(PermissionConstants.STORAGE))) {
            mLackedPermission.add(PermissionConstants.STORAGE);
        }
        if (null != mLackedPermission && !mLackedPermission.isEmpty()) {
            PermissionUtils.permission(mLackedPermission.toArray(new String[0])).callback(new PermissionUtils.FullCallback() {
                @Override
                public void onGranted(List<String> permissionsGranted) {
                    permissionsGranted.containsAll(Arrays.asList(PermissionConstants.getPermissions(PermissionConstants.STORAGE)));//  DownLoadManager.with().init(MainActivity.this);
                }

                @Override
                public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                    LogUtils.eTag("mosr", "onPermissionDenied");
                }
            }).request();
        }
    }

    @Override
    public void initView() {
        CacheDiskStaticUtils.put(ExtraConstant.KEY_GUIDE_FLAG, "1");
        saveData();
//        loginByAddress();
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
        pictureSortFragment = (PictureSortFragment) getSupportFragmentManager().findFragmentByTag("pictureSortFragment");
        creatorFragment = (CreatorFragment) getSupportFragmentManager().findFragmentByTag("creatorFragment");
        blindBoxFragment = (BlindBoxFragment) getSupportFragmentManager().findFragmentByTag("blindBoxFragment");
        mineFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag("mineFragment");
        if (null == mCurrentFragment) {
            switch (mCurrentItemId) {
                case R.id.navigation_home:
                    mCurrentFragment = homeFragment;
                    break;
                case R.id.navigation_art_sort:
                    mCurrentFragment = pictureSortFragment;
                    break;
                case R.id.navigation_creator:
                    mCurrentFragment = creatorFragment;
                    break;
                case R.id.navigation_shop_cart:
                    mCurrentFragment = blindBoxFragment;
                    break;
                case R.id.navigation_mine:
                    mCurrentFragment = mineFragment;
                    break;
            }
        }


        mBottomNavigationView = findViewById(R.id.mBottomNavigationView);

        mBottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            if (null == getSupportFragmentManager()) {
                return false;
            }

            if (mCurrentItemId == menuItem.getItemId()) {
                return false;
            }
            mCurrentItemId = menuItem.getItemId();
            FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (null != mCurrentFragment) {
                mFragmentTransaction.hide(mCurrentFragment);
            }
            switch (mCurrentItemId) {
                case R.id.navigation_home:
                    if (null == homeFragment) {
                        if (null != getSupportFragmentManager().findFragmentByTag("homeFragment")) {
                            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
                        } else {
                            homeFragment = (HomeFragment) HomeFragment.newInstance();
                            mFragmentTransaction.add(R.id.container, homeFragment, "homeFragment");
                        }
                    }
                    mCurrentFragment = homeFragment;
                    break;
                case R.id.navigation_art_sort:
                    if (null == pictureSortFragment) {
                        if (null != getSupportFragmentManager().findFragmentByTag("pictureSortFragment")) {
                            pictureSortFragment = (PictureSortFragment) getSupportFragmentManager().findFragmentByTag("pictureSortFragment");
                        } else {
                            pictureSortFragment = (PictureSortFragment) PictureSortFragment.newInstance();
                            mFragmentTransaction.add(R.id.container, pictureSortFragment, "pictureSortFragment");
                        }
                    }
                    mCurrentFragment = pictureSortFragment;
                    break;

                case R.id.navigation_shop_cart:
                    if (null == blindBoxFragment) {
                        if (null != getSupportFragmentManager().findFragmentByTag("blindBoxFragment")) {
                            blindBoxFragment = (BlindBoxFragment) getSupportFragmentManager().findFragmentByTag("blindBoxFragment");
                        } else {
                            blindBoxFragment = (BlindBoxFragment) BlindBoxFragment.newInstance();
                            mFragmentTransaction.add(R.id.container, blindBoxFragment, "blindBoxFragment");
                        }
                    }
                    mCurrentFragment = blindBoxFragment;
                    break;
                case R.id.navigation_creator:
                    if (null == creatorFragment) {
                        if (null != getSupportFragmentManager().findFragmentByTag("creatorFragment")) {
                            creatorFragment = (CreatorFragment) getSupportFragmentManager().findFragmentByTag("creatorFragment");
                        } else {
                            creatorFragment = (CreatorFragment) CreatorFragment.newInstance();
                            mFragmentTransaction.add(R.id.container, creatorFragment, "creatorFragment");
                        }
                    }
                    mCurrentFragment = creatorFragment;
                    break;
                case R.id.navigation_mine:
                    if (null == mineFragment) {
                        if (null != getSupportFragmentManager().findFragmentByTag("mineFragment")) {
                            mineFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag("mineFragment");
                        } else {
                            mineFragment = (MineFragment) MineFragment.newInstance();
                            mFragmentTransaction.add(R.id.container, mineFragment, "mineFragment");
                        }
                    }
                    mCurrentFragment = mineFragment;
                    break;
            }
//                setAndroidNativeLightStatusBar(mCurrentItemId != R.id.navigation_mine, false, true);
            if (null != mCurrentFragment) {
                mFragmentTransaction.show(mCurrentFragment);
            }
            mFragmentTransaction.commitAllowingStateLoss();
            return true;
        });

        mBottomNavigationView.setSelectedItemId(0 != mCurrentItemId ? mCurrentItemId : R.id.navigation_home);
        mBottomNavigationView.setItemIconTintList(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(com.upbest.arouter.EventBusMessageEvent eventBusMessageEvent) {
        if (eventBusMessageEvent != null) {
            if (eventBusMessageEvent.getmMessage().equals(EventEntity.EVENT_REFRESH_TOKEN)) {
                //refresh token
                LogUtils.e("refresh token===");
                loginByAddress();
            }
        }
    }

    public void loginByAddress() {
        String privateKey = SharedPreUtils.getString(this, SharedPreUtils.KEY_PRIVATE);
        String publicKey = SharedPreUtils.getString(this, SharedPreUtils.KEY_PUBLICKEY);
        String nonce = SharedPreUtils.getString(this, SharedPreUtils.KEY_NONCE);
        String Address = SharedPreUtils.getString(this, SharedPreUtils.KEY_ADDRESS);
        LogUtils.e(privateKey + "|" + publicKey + "|" + nonce);
        Keypair keypair = new Keypair(Hex.decode(privateKey), Hex.decode(publicKey), Hex.decode(nonce.substring(2)));
        Signer signer = new Signer();
        SignatureWrapper signatureWrapper = signer.sign(EncryptionType.SR25519, Address.getBytes(), keypair);
        String singStr2 = Hex.toHexString(signatureWrapper.getSignature());
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("address", Address);
        hashMap.put("message", Address);
        hashMap.put("signature", singStr2);
        hashMap.put("cid", PushManager.getInstance().getClientid(this));
        hashMap.put("os", "android");
        RequestManager.instance().addressLogin(hashMap, new MinerCallback<BaseResponseVo<UserVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null)
                        if (response.body().getBody() != null) {
                            UserVo userVo = response.body().getBody();
                            YunApplication.setmUserVo(userVo);
                            YunApplication.setToken(userVo.getToken());
                        }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(AppConstant.HOME_CURRENT_ITEM_ID, mCurrentItemId);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessageEvent mEventBusMessageEvent) {
        if (null != mEventBusMessageEvent && !TextUtils.isEmpty(mEventBusMessageEvent.getmMessage())) {
            if (TextUtils.equals(ExtraConstant.EVENT_SKIP_HOME, mEventBusMessageEvent.getmMessage())) {
                if (null != mBottomNavigationView) {
                    mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
                }
            } else if (TextUtils.equals(ExtraConstant.EVENT_PUSH, mEventBusMessageEvent.getmMessage())) {
                pushNotifiction(mEventBusMessageEvent.getmValue().toString());
            } else if (TextUtils.equals(ExtraConstant.EVENT_MORE_PICTUR_SELECT, mEventBusMessageEvent.getmMessage())) {
                mBottomNavigationView.setSelectedItemId(R.id.navigation_art_sort);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsback) {
                EventBus.getDefault().post(new com.upbest.arouter.EventBusMessageEvent(EventEntity.EVENT_FINISH, ""));
                finish();
            } else {
                //监听全屏视频时返回键
                mIsback = true;
                ToastUtils.showShort(R.string.double_click_exit);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsback = false;
                    }
                }, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        intentSkip(intent);
    }

    public void intentSkip(Intent mIntent) {
        if (null == mIntent) {
            return;
        }
        if (null == mIntent.getStringExtra(JUMP_PAGE)) {
            return;
        }
        String jumpPage = mIntent.getStringExtra(JUMP_PAGE);
        switch (jumpPage) {
            case "0":
                if (null != mBottomNavigationView) {
                    mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
                }
                break;
            case "1":
                if (null != mBottomNavigationView) {
                    mBottomNavigationView.setSelectedItemId(R.id.navigation_art_sort);
                }
                break;
            case "2":
                if (null != mBottomNavigationView) {
                    mBottomNavigationView.setSelectedItemId(R.id.navigation_creator);
                }
                break;
        }
    }

    boolean hasJump = false;

    @Override
    protected void onResume() {
        super.onResume();
        UserManager.isLogin(false);
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @NeedsPermission({Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void needPermission() {

    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void permissionDenied() {
    }

    @OnNeverAskAgain({Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void neverAsk() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE};
        PermissionDialog permissionDialog
                = new PermissionDialog(MainActivity.this, permission);
        permissionDialog.show();
    }

    private void saveData() {

        if (!TextUtils.isEmpty(Extras.Address)) {
            SharedPreUtils.setString(this, SharedPreUtils.KEY_ADDRESS, Extras.Address);
        }
        if (!TextUtils.isEmpty(Extras.publicKey)) {
            SharedPreUtils.setString(this, SharedPreUtils.KEY_PUBLICKEY, Extras.publicKey);
        }
        if (!TextUtils.isEmpty(Extras.seed)) {
            SharedPreUtils.setString(this, SharedPreUtils.KEY_SEED, Extras.seed);
        }
        if (!TextUtils.isEmpty(Extras.nonce)) {
            SharedPreUtils.setString(this, SharedPreUtils.KEY_NONCE, Extras.nonce);
        }
        if (!TextUtils.isEmpty(Extras.privateKey)) {
            SharedPreUtils.setString(this, SharedPreUtils.KEY_PRIVATE, Extras.privateKey);
        }
        if (!TextUtils.isEmpty(Extras.pinCode)) {
            SharedPreUtils.setString(this, SharedPreUtils.KEY_PIN, Extras.pinCode);
        }
    }

    ReceiverPushBean receiverPushBean;

    private void pushNotifiction(String json) {
        LogUtils.e("json = " + json);
        hasJump = false;
        Gson gson = new Gson();
        receiverPushBean = gson.fromJson(json, ReceiverPushBean.class);
        NotificationUtil notificationUtil = new NotificationUtil(getApplicationContext());
        notificationUtil.sendNotification(receiverPushBean);
    }

}