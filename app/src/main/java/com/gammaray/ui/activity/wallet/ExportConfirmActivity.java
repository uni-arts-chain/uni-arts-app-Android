package com.gammaray.ui.activity.wallet;


import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.FragmentTransaction;

import com.upbest.arouter.EventBusMessageEvent;
import com.upbest.arouter.EventEntity;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.databinding.ActivityAcountBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jp.co.soramitsu.feature_account_impl.presentation.exporting.json.confirm.ExportJsonConfirmFragment;
import jp.co.soramitsu.feature_account_impl.presentation.exporting.json.confirm.ExportJsonConfirmPayload;

public class ExportConfirmActivity extends BaseActivity<ActivityAcountBinding> {
    FragmentTransaction mFragmentTransaction;
    ExportJsonConfirmFragment otcFragment;
    ExportJsonConfirmFragment exportJsonConfirmFragment;
    ExportJsonConfirmPayload exportJsonConfirmPayload;

    @Override
    public int getLayoutId() {
        return R.layout.activity_acount;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        exportJsonConfirmPayload = getIntent().getParcelableExtra("data");
        EventBus.getDefault().register(this);
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        otcFragment = new ExportJsonConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("PAYLOAD_KEY", exportJsonConfirmPayload);
        otcFragment.setArguments(bundle);
        mFragmentTransaction.add(R.id.container, otcFragment, "otcFragment");
        mFragmentTransaction.show(otcFragment);
        mFragmentTransaction.commitAllowingStateLoss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusMessageEvent eventBusMessageEvent) {
        if (eventBusMessageEvent != null) {
            if (TextUtils.equals(EventEntity.EVENT_EXPORT_JSON, eventBusMessageEvent.getmMessage())) {
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