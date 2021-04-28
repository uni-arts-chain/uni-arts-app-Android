package com.yunhualian.ui.activity;


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.databinding.ActivityAcountBinding;
import com.yunhualian.utils.SharedPreUtils;


import jp.co.soramitsu.feature_account_impl.presentation.exporting.seed.ExportSeedFragment;

public class ExportActivity extends BaseActivity<ActivityAcountBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_acount;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
        ExportSeedFragment otcFragment = new ExportSeedFragment();
        otcFragment.setAccountAddress(SharedPreUtils.KEY_ADDRESS);
        mFragmentTransaction.add(R.id.container, otcFragment, "");
        mFragmentTransaction.show(otcFragment);
        mFragmentTransaction.commitAllowingStateLoss();
    }
}