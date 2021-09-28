package com.gammaray.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ToastUtils;
import com.gammaray.R;
import com.gammaray.adapter.MyHomePageAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.constant.AppConstant;
import com.gammaray.databinding.FragmentFindLayoutBinding;
import com.gammaray.ui.activity.DAppSearchActivity;
import com.gammaray.ui.activity.DAppsListActivity;
import com.gammaray.ui.activity.QrScanActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;

public class FindFragment extends BaseFragment<FragmentFindLayoutBinding> {


    public static BaseFragment newInstance() {
        return new FindFragment();
    }

    private MyHomePageAdapter mRecentPageAdapter;

    private MyHomePageAdapter mAllPageAdapter;

    private int mViewAppPos = 0;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_find_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

        initRecentDApp();
        initAllDApp();

        mBinding.tvAllApp.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), DAppsListActivity.class);
            if (mViewAppPos == 0) {
                intent.putExtra("title", "收藏");
                intent.putExtra("type", 0);
            } else {
                intent.putExtra("title", "最近");
                intent.putExtra("type", 1);
            }
            startActivity(intent);
        });

        mBinding.tvGoSearch.setOnClickListener(view -> {
            startActivity(DAppSearchActivity.class);
        });

        mBinding.rlSaoma.setOnClickListener(view -> {
            scan();
        });
    }

    private void initRecentDApp() {
        mRecentPageAdapter = new MyHomePageAdapter(getChildFragmentManager(), 2, Arrays.asList(getResources().getStringArray(R.array.recent_dapps)), requireActivity());
        mRecentPageAdapter.setListener(position -> {
            if (position == 0) {
                return RecentDAppFragment.newInstance("collect_app");
            } else {
                return RecentDAppFragment.newInstance("recent_app");
            }
        });

        mBinding.vpViewApps.setAdapter(mRecentPageAdapter);
        mBinding.tabRecentApps.setupWithViewPager(mBinding.vpViewApps);
        mBinding.tabRecentApps.setTabMode(TabLayout.MODE_FIXED);

        mBinding.vpViewApps.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mViewAppPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initAllDApp() {
        mAllPageAdapter = new MyHomePageAdapter(getChildFragmentManager(), 2, Arrays.asList(getResources().getStringArray(R.array.all_dapps)), requireActivity());
        mAllPageAdapter.setListener(position -> {
            if (position == 0) {
                return DAppListFragment.newInstance("ETH");
            } else {
                return DAppListFragment.newInstance("UART");
            }
        });
        mBinding.vpAllApps.setAdapter(mAllPageAdapter);
        mBinding.tabDpps.setupWithViewPager(mBinding.vpAllApps);
        mBinding.tabDpps.setTabMode(TabLayout.MODE_FIXED);

    }

    private void scan() {

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // android 6.0以上需要动态申请权限
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, AppConstant.REQ_PERM_CAMERA);
            return;
        }
        // 访问手机存储申请权限
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // android 6.0以上需要动态申请权限
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(requireActivity(), QrScanActivity.class);
        startActivityForResult(intent, AppConstant.REQ_QR_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == AppConstant.REQ_QR_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //扫描二维码或者扫描相册成功
                String result = data.getStringExtra("scan_result");
                ToastUtils.showShort("扫描成功" + result);
            } else {
                ToastUtils.showShort("取消扫描");
            }
        }
    }
}
