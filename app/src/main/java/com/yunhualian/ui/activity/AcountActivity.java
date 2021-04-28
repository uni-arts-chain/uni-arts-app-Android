package com.yunhualian.ui.activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.blankj.utilcode.util.ToastUtils;
import com.upbest.arouter.EventBusMessageEvent;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.databinding.ActivityAcountBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jp.co.soramitsu.feature_wallet_impl.presentation.balance.list.BalanceListFragment;

public class AcountActivity extends BaseActivity<ActivityAcountBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_acount;
    }

    @Override
    public void initPresenter() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusMessageEvent eventBusMessageEvent) {
        if (eventBusMessageEvent != null) {
            ToastUtils.showLong("addr" + eventBusMessageEvent.getmValue());
        }
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
        BalanceListFragment otcFragment = new BalanceListFragment();
        mFragmentTransaction.add(R.id.container, otcFragment, "");
        mFragmentTransaction.show(otcFragment);
        mFragmentTransaction.commitAllowingStateLoss();
    }
}