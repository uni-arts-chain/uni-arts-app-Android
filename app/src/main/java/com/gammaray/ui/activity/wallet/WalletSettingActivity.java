package com.gammaray.ui.activity.wallet;


import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.upbest.arouter.ArouterModelPath;
import com.upbest.arouter.Extras;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityWalletSettingBinding;
import com.gammaray.ui.activity.PinCodeKtActivity;
import com.gammaray.utils.SharedPreUtils;
import com.gammaray.widget.PrivateKeyDerivetDialog;

import java.math.BigDecimal;

@Route(path = ArouterModelPath.EXPORT_KEYSTORE)
public class WalletSettingActivity extends BaseActivity<ActivityWalletSettingBinding> implements View.OnClickListener {

    public static int KEY_STORE = 0;

    public static int KEY_PRIVATE = 1;
    private PrivateKeyDerivetDialog privateKeyDerivetDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_setting;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.edit_wallet;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        mDataBinding.editPsw.setOnClickListener(this);
        mDataBinding.exportPrivateKey.setOnClickListener(this);
        mDataBinding.exportKeyStore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.editPsw:

                break;
            case R.id.exportPrivateKey:
                startActivityForResult(PinCodeKtActivity.class, KEY_PRIVATE);
                break;
            case R.id.exportKeyStore:
                startActivityForResult(PinCodeKtActivity.class, KEY_STORE);
                break;

        }

    }

    public void showDerivePrivateKeyDialog(String privateKey) {
        if (privateKey.charAt(0) != '0' && privateKey.charAt(1) != 'x') {
            privateKey = "0x".concat(privateKey);
        }
        privateKeyDerivetDialog = new PrivateKeyDerivetDialog(WalletSettingActivity.this);
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
        }

    }
}