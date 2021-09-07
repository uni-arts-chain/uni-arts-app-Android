package com.gammaray.ui.activity.wallet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.ToastUtils;
import com.upbest.arouter.EventBusMessageEvent;
import com.upbest.arouter.EventEntity;
import com.upbest.arouter.Extras;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityAcountBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import jp.co.soramitsu.feature_account_impl.presentation.mnemonic.confirm.ConfirmMnemonicFragment;
import jp.co.soramitsu.feature_account_impl.presentation.mnemonic.confirm.ConfirmMnemonicPayload;

public class ConfirmMnemonicActivity extends BaseActivity<ActivityAcountBinding> {

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
            if (eventBusMessageEvent.getmMessage().equals(EventEntity.EVENT_ADDRESS_COPY)) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                if (cm != null) {
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", eventBusMessageEvent.getmValue().toString());
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    ToastUtils.showShort("复制成功");
                }
            } else if (eventBusMessageEvent.getmMessage().equals(EventEntity.EVENT_BACKUP_SUCCESS)) {
                finish();
            }
        }
    }

    @Override
    public void initView() {
        List<String> list = getIntent().getStringArrayListExtra("list");
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.count_bage;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        EventBus.getDefault().register(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Extras.isShow = true;
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
        ConfirmMnemonicFragment otcFragment = new ConfirmMnemonicFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("confirm_payload", new ConfirmMnemonicPayload(list, null));
        otcFragment.setArguments(bundle);
        mFragmentTransaction.add(R.id.container, otcFragment, "");
        mFragmentTransaction.show(otcFragment);
        mFragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Extras.isShow = false;
        EventBus.getDefault().unregister(this);
    }


}