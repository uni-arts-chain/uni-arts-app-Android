package com.gammaray.ui.activity.wallet;


import android.os.Bundle;
import android.text.TextUtils;

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

import jp.co.soramitsu.feature_account_impl.presentation.exporting.json.confirm.ExportJsonConfirmFragment;
import jp.co.soramitsu.feature_account_impl.presentation.exporting.json.confirm.ExportJsonConfirmPayload;
import jp.co.soramitsu.feature_account_impl.presentation.exporting.json.password.ExportJsonPasswordFragment;

public class ExportActivity extends BaseActivity<ActivityAcountBinding> {
    FragmentTransaction mFragmentTransaction;
    ExportJsonPasswordFragment otcFragment;
    ExportJsonConfirmFragment exportJsonConfirmFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_acount;
    }

    @Override
    public void initPresenter() {

    }

    public void initToolBar() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.derive_keystore_title;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
    }

    @Override
    public void initView() {
        initToolBar();
        EventBus.getDefault().register(this);
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        otcFragment = new ExportJsonPasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ACCOUNT_ADDRESS_KEY", SharedPreUtils.getString(this, SharedPreUtils.KEY_ADDRESS));
        otcFragment.setArguments(bundle);
        mFragmentTransaction.add(R.id.container, otcFragment, "otcFragment");
        mFragmentTransaction.show(otcFragment);
        mFragmentTransaction.commitAllowingStateLoss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusMessageEvent eventBusMessageEvent) {
        if (eventBusMessageEvent != null) {
            if (TextUtils.equals(EventEntity.EVENT_EXPORT_JSON, eventBusMessageEvent.getmMessage())) {
                ExportJsonConfirmPayload exportJsonConfirmPayload = (ExportJsonConfirmPayload) eventBusMessageEvent.getmValue();
                if (exportJsonConfirmPayload != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("json", exportJsonConfirmPayload.getJson());
                    startActivity(ExportKeystoreActivity.class, bundle);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}