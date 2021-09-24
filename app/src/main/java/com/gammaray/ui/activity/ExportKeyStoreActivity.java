package com.gammaray.ui.activity;

import androidx.fragment.app.Fragment;

import com.gammaray.R;
import com.gammaray.adapter.MyHomePageAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityExportKeystoreLayoutBinding;
import com.gammaray.ui.fragment.ExportKeystoreQRCodeFragment;
import com.gammaray.ui.fragment.ExportKeystoreStringFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;

public class ExportKeyStoreActivity extends BaseActivity<ActivityExportKeystoreLayoutBinding> implements MyHomePageAdapter.TabPagerListener {

    private MyHomePageAdapter mAdapter;
    private String mKeyStore;

    @Override
    public int getLayoutId() {
        return R.layout.activity_export_keystore_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions toolBarOptions = new ToolBarOptions();
        toolBarOptions.titleString = "导出KeyStore";
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, toolBarOptions);

        if (getIntent() != null) {
            mKeyStore = getIntent().getStringExtra("walletKeystore");
        }

        mAdapter = new MyHomePageAdapter(getSupportFragmentManager(), 2, Arrays.asList(getResources().getStringArray(R.array.backup_keystore)), this);
        mAdapter.setListener(this);
        mDataBinding.viewpager.setAdapter(mAdapter);
        mDataBinding.tabLayout.setupWithViewPager(mDataBinding.viewpager);
        mDataBinding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public Fragment getFragment(int position) {
        if(position == 0){
            return ExportKeystoreStringFragment.newInstance(mKeyStore);
        }else{
            return ExportKeystoreQRCodeFragment.newInstance(mKeyStore);
        }
    }
}
