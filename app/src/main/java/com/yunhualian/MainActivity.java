package com.yunhualian;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.upbest.arouter.ArouterModelPath;
import com.upbest.arouter.EventEntity;
import com.upbest.arouter.Extras;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.constant.AppConstant;
import com.yunhualian.constant.ExtraConstant;
import com.yunhualian.databinding.ActivityMainBinding;
import com.yunhualian.entity.EventBusMessageEvent;
import com.yunhualian.entity.ReceiverPushBean;
import com.yunhualian.ui.activity.blindbox.BlindBoxDetailActivity;
import com.yunhualian.ui.activity.order.SellAndBuyActivity;
import com.yunhualian.ui.activity.user.MyHomePageActivity;
import com.yunhualian.ui.fragment.BlindBoxFragment;
import com.yunhualian.ui.fragment.CreatorFragment;
import com.yunhualian.ui.fragment.HomeFragment;
import com.yunhualian.ui.fragment.MineFragment;
import com.yunhualian.ui.fragment.PictureSortFragment;
import com.yunhualian.utils.NotificationUtil;
import com.yunhualian.utils.SharedPreUtils;
import com.yunhualian.utils.ToastManager;
import com.yunhualian.utils.UserManager;
import com.yunhualian.widget.PermissionDialog;
import com.yunhualian.widget.UpdateDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import jp.co.soramitsu.fearless_utils.encrypt.model.Keypair;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;

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
    public static final String ART = "art";
    public static final String TRADE = "trade";
    public static final String BLINDBOX = "blind_box";
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
        // DownLoadManager.with().init(this);
    }

    @Override
    public void initView() {

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
                ToastManager.showShort(R.string.double_click_exit);
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
//        Uri data = mIntent.getData();
//        String jumpPage = data.getQueryParameter("");
//        if (TextUtils.isEmpty(jumpPage)) {
//            return;
//        }
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
//                String parameters = data.getQueryParameter("parameters");
//                if (TextUtils.isEmpty(parameters)) {
//                    break;
//                }
//                try {
//                    JSONObject mJsonObject = new JSONObject(parameters);
//                    String hotLabel = mJsonObject.optString("hotLabel");
//                    String generalSort = mJsonObject.optString("generalSort");
//                    String quotaSort = mJsonObject.optString("quotaSort");
//                    String cycleSort = mJsonObject.optString("cycleSort");
//
////                    if (null != mLoanFragment) {
////                        mLoanFragment.skipFilterProduct(hotLabel, generalSort, quotaSort, cycleSort);
////                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
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
//        boolean isGoPage = getIntent().getBooleanExtra(NotificationUtil.KEY, false);
//        ReceiverPushBean receiverPushBean = (ReceiverPushBean) getIntent().getSerializableExtra(NotificationUtil.DATA);
//        if (isGoPage && !hasJump) {
//            goPage(receiverPushBean);
//        }
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

    public void signerKey(Keypair keypari) {
        try {
            Class<?> clz = Class.forName("jp.co.soramitsu.fearless_utils.encrypt.Signer");
            //获取object实例，即AppInfo实例，这里会调用无参构造方法
            Object obj = clz.newInstance();
            //调用方法setAppName，传入String 类型的参数
            Method mothod1 = clz.getMethod("signSr25519", String.class);
            //执行该方法，实参为"hhhhh"，obj为要操作的对象
            String str = mothod1.invoke(obj, "", keypari).toString();
            //调用getAppName 方法，不需要传参
            LogUtils.e(str);
        } catch (Exception e) {
        }

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
//        if (!TextUtils.isEmpty(Extras.privateKey)
//                && !TextUtils.isEmpty(Extras.publicKey) && !TextUtils.isEmpty(Extras.nonce) && !TextUtils.isEmpty(Extras.Address)) {
//            loginByAddress(Extras.privateKey, Extras.nonce, Extras.nonce, Extras.Address);
//        }
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

    private void goPage(ReceiverPushBean receiverPushBean) {
        hasJump = true;
        if (receiverPushBean != null) {
            String[] parms = receiverPushBean.getPayload().split("#");
            if (parms.length > 2) {
                String resource = parms[2];
                switch (resource) {
                    case ART:
                        startActivity(MyHomePageActivity.class);
                        break;
                    case TRADE:
                        Bundle sell = new Bundle();
                        sell.putString("from", SellAndBuyActivity.SELL);
                        startActivity(SellAndBuyActivity.class, sell);
                        break;
                    case BLINDBOX:
                        String blindBoxId = parms[3];
                        Bundle bundle = new Bundle();
                        bundle.putString("id", blindBoxId);
                        startActivity(BlindBoxDetailActivity.class, bundle);
                        break;
                }
            }
        }
    }
}