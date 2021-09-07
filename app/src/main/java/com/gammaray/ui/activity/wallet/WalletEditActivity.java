package com.gammaray.ui.activity.wallet;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.upbest.arouter.ArouterModelPath;
import com.upbest.arouter.EventBusMessageEvent;
import com.upbest.arouter.EventEntity;
import com.upbest.arouter.Extras;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.base.YunApplication;
import com.gammaray.databinding.ActivityAcountBinding;
import com.gammaray.ui.activity.PinCodeKtActivity;
import com.gammaray.ui.activity.UserAgreementActivity;
import com.gammaray.utils.SharedPreUtils;
import com.gammaray.widget.PrivateKeyDerivetDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;

import jp.co.soramitsu.feature_account_impl.presentation.account.details.AccountDetailsFragment;

@Route(path = ArouterModelPath.EDIT_WALLET)
public class WalletEditActivity extends BaseActivity<ActivityAcountBinding> {

    public static int KEY_STORE = 0;
    public static final String RESUME_CER = "resume";
    public static final String SET_CER = "set";
    public static int KEY_PRIVATE = 1;
    public static int KEY_PSW = 2;
    public static int KEY_PSW_CONFIRM = 3;
    public static int KEY_BACKUP = 4;

    @Override
    public int getLayoutId() {
        return R.layout.activity_acount;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.edit_wallet;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
        if (TextUtils.isEmpty(Extras.headUrl)) {
            if (YunApplication.getmUserVo() != null)
                if (YunApplication.getmUserVo().getAvatar() != null)
                    Extras.headUrl = YunApplication.getmUserVo().getAvatar().getUrl();
        }
        AccountDetailsFragment otcFragment = new AccountDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ACCOUNT_ADDRESS_KEY", SharedPreUtils.getString(this, SharedPreUtils.KEY_ADDRESS));
        otcFragment.setArguments(bundle);
        mFragmentTransaction.add(R.id.container, otcFragment, "");
        mFragmentTransaction.show(otcFragment);
        mFragmentTransaction.commitAllowingStateLoss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusMessageEvent eventBusMessageEvent) {
        if (eventBusMessageEvent != null && eventBusMessageEvent.getmValue() != null) {
            if (TextUtils.equals(EventEntity.EVENT_SAVE, eventBusMessageEvent.getmValue().toString())) {
                ToastUtils.showShort("保存成功");
//                finish();
            } else if (TextUtils.equals(EventEntity.EVENT_PSW, eventBusMessageEvent.getmValue().toString())) {
                Intent intent = new Intent(this, PinCodeKtActivity.class);
                intent.putExtra(RESUME_CER, true);
                startActivityForResult(intent, KEY_PSW_CONFIRM);
            } else if (TextUtils.equals(EventEntity.EVENT_PRIVATE_KEY, eventBusMessageEvent.getmValue().toString())) {
                startActivityForResult(PinCodeKtActivity.class, KEY_PRIVATE);
            } else if (TextUtils.equals(EventEntity.EVENT_KEY_STORE, eventBusMessageEvent.getmValue().toString())) {
                startActivityForResult(PinCodeKtActivity.class, KEY_STORE);
            } else if (TextUtils.equals(EventEntity.EVENT_BACKUP, eventBusMessageEvent.getmValue().toString())) {
                startActivityForResult(PinCodeKtActivity.class, KEY_BACKUP);
            } else if (TextUtils.equals(EventEntity.EVENT_PROTOCAL, eventBusMessageEvent.getmValue().toString())) {
                //TODO 隐私协议
                startActivity(UserAgreementActivity.class);
            }
        }
    }

    public void showDerivePrivateKeyDialog(String privateKey) {
        if (privateKey.charAt(0) != '0' && privateKey.charAt(1) != 'x') {
            privateKey = "0x".concat(privateKey);
        }
        PrivateKeyDerivetDialog privateKeyDerivetDialog = new PrivateKeyDerivetDialog(this);
        privateKeyDerivetDialog.show();
        privateKeyDerivetDialog.setPrivateKey(privateKey);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEY_STORE) {
            if (resultCode == BigDecimal.ONE.intValue()) {
                Extras.keyStorePsw = SharedPreUtils.getString(this, SharedPreUtils.KEY_PIN);
                startActivity(ExportActivity.class);
            }
        } else if (requestCode == KEY_PRIVATE) {
            if (resultCode == BigDecimal.ONE.intValue()) {
                showDerivePrivateKeyDialog(SharedPreUtils.getString(this, SharedPreUtils.KEY_SEED));
            }
        } else if (requestCode == KEY_PSW) {

        } else if (requestCode == KEY_PSW_CONFIRM) {
            if (resultCode == BigDecimal.ONE.intValue()) {
                ToastUtils.showShort("密码修改成功");
            }
        } else if (requestCode == KEY_BACKUP) {
            if (resultCode == BigDecimal.ONE.intValue())
                startActivity(BackUpActivity.class);
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

}