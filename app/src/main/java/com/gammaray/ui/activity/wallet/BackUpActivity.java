package com.gammaray.ui.activity.wallet;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.upbest.arouter.EventBusMessageEvent;
import com.upbest.arouter.EventEntity;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityAcountBinding;
import com.gammaray.utils.SharedPreUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import jp.co.soramitsu.feature_account_impl.presentation.exporting.mnemonic.ExportMnemonicFragment;

public class BackUpActivity extends BaseActivity<ActivityAcountBinding> {
    private ArrayList<String> mnemonicList;

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
            if (eventBusMessageEvent.getmMessage().equals(EventEntity.EVENT_BACKUP_MNEMONIC)) {
                mnemonicList = (ArrayList<String>) eventBusMessageEvent.getmValue();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("list", mnemonicList);
                startActivity(ConfirmMnemonicActivity.class, bundle);
            } else if (eventBusMessageEvent.getmMessage().equals(EventEntity.EVENT_BACKUP_SUCCESS)) {
                finish();
            }
        }
    }

    @Override
    public void initView() {

        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.back_up_mnemonic_title;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        EventBus.getDefault().register(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
        ExportMnemonicFragment otcFragment = new ExportMnemonicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ACCOUNT_ADDRESS_KEY", SharedPreUtils.getString(this, SharedPreUtils.KEY_ADDRESS));
        otcFragment.setArguments(bundle);
        mFragmentTransaction.add(R.id.container, otcFragment, "");
        mFragmentTransaction.show(otcFragment);
        mFragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}