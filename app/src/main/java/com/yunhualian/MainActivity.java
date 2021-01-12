package com.yunhualian;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.constant.AppConstant;
import com.yunhualian.constant.ExtraConstant;
import com.yunhualian.entity.EventBusMessageEvent;
import com.yunhualian.ui.fragment.CreatorFragment;
import com.yunhualian.ui.fragment.HomeFragment;
import com.yunhualian.ui.fragment.MineFragment;
import com.yunhualian.ui.fragment.PictureSortFragment;
import com.yunhualian.ui.fragment.ShoppingCartFragment;
import com.yunhualian.utils.DownLoadManager;
import com.yunhualian.utils.ToastManager;
import com.yunhualian.utils.UserManager;
import com.yunhualian.widget.PermissionDialog;
import com.yunhualian.widget.UpdateDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity {
    private FrameLayout container;
    private CoordinatorLayout main_content;
    private HomeFragment homeFragment;
    private PictureSortFragment pictureSortFragment;
    private ShoppingCartFragment shoppingCartFragment;
    private MineFragment mineFragment;
    private CreatorFragment creatorFragment;
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
                    if (permissionsGranted.containsAll(Arrays.asList(PermissionConstants.getPermissions(PermissionConstants.STORAGE)))) {
                      //  DownLoadManager.with().init(MainActivity.this);
                    }
                }

                @Override
                public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                    LogUtils.eTag("mosr", "onPermissionDenied");
                }
            }).request();
            return;
        }
       // DownLoadManager.with().init(this);
    }

    @Override
    public void initView() {
//        setAndroidNativeLightStatusBar(false, false, false);
//        getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG,
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

//        SetTranslanteBar();
//        SetStatusBarColor();
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
        pictureSortFragment = (PictureSortFragment) getSupportFragmentManager().findFragmentByTag("pictureSortFragment");
        creatorFragment = (CreatorFragment) getSupportFragmentManager().findFragmentByTag("creatorFragment");
        shoppingCartFragment = (ShoppingCartFragment) getSupportFragmentManager().findFragmentByTag("shoppingCartFragment");
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
                    mCurrentFragment = shoppingCartFragment;
                    break;
                case R.id.navigation_mine:
                    mCurrentFragment = mineFragment;
                    break;
            }
        }


        container = (FrameLayout) findViewById(R.id.container);
        main_content = (CoordinatorLayout) findViewById(R.id.main_content);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.mBottomNavigationView);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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
                    case R.id.navigation_shop_cart:
                        if (null == shoppingCartFragment) {
                            if (null != getSupportFragmentManager().findFragmentByTag("shoppingCartFragment")) {
                                shoppingCartFragment = (ShoppingCartFragment) getSupportFragmentManager().findFragmentByTag("shoppingCartFragment");
                            } else {
                                shoppingCartFragment = (ShoppingCartFragment) ShoppingCartFragment.newInstance();
                                mFragmentTransaction.add(R.id.container, shoppingCartFragment, "shoppingCartFragment");
                            }
                        }
                        mCurrentFragment = shoppingCartFragment;
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
            }
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
}