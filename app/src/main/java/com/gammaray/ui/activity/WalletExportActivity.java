package com.gammaray.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityWalletExportLayoutBinding;
import com.gammaray.eth.domain.ETHWallet;
import com.gammaray.eth.interact.ModifyWalletInteract;
import com.gammaray.widget.PrivateKeyDerivetDialog;

import java.math.BigDecimal;

public class WalletExportActivity extends BaseActivity<ActivityWalletExportLayoutBinding> implements View.OnClickListener {

    private String mWalletName;
    public static int KEY_PRIVATE = 1;
    public static int KEY_PSW = 2;
    public static int KEY_PSW_CONFIRM = 3;
    public static int KEY_BACKUP = 4;
    private ModifyWalletInteract modifyWalletInteract;
    private ETHWallet ethWallet;
    private long walletId;
    private String walletPwd;
    private String walletAddress;
    private String walletName;
    private boolean walletIsBackup;
    private String walletMnemonic;
    private boolean fromList;
    private boolean isCurrent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_export_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions toolBarOptions = new ToolBarOptions();
        toolBarOptions.titleString = "编辑钱包";
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, toolBarOptions);

        modifyWalletInteract = new ModifyWalletInteract();

        if (getIntent() != null) {
            mWalletName = getIntent().getStringExtra("walletName");
            walletId = getIntent().getLongExtra("walletId", -1);
            walletPwd = getIntent().getStringExtra("walletPwd");
            walletAddress = getIntent().getStringExtra("walletAddress");
            walletIsBackup = getIntent().getBooleanExtra("walletIsBackup", false);
            walletMnemonic = getIntent().getStringExtra("walletMnemonic");
            fromList = getIntent().getBooleanExtra("fromList", false);
            isCurrent = getIntent().getBooleanExtra("isCurrent", true);
        }
        mDataBinding.accountDetailsNameField.getContent().setText(mWalletName);

        mDataBinding.editPsw.setOnClickListener(this);
        mDataBinding.exportPrivateKey.setOnClickListener(this);
        mDataBinding.protocal.setOnClickListener(this);
        mDataBinding.backupMnemonic.setOnClickListener(this);
        mDataBinding.saveAction.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.editPsw) {
            //修改密码
            Intent intent = new Intent(WalletExportActivity.this, ETHPinCodeActivity.class);
            intent.putExtra("resume", true);
            intent.putExtra("wallet_pwd", walletPwd);
            startActivityForResult(intent, KEY_PSW_CONFIRM);
        } else if (view.getId() == R.id.exportPrivateKey) {
            //导出私钥
            startActivityForResult(ETHPinCodeActivity.class, KEY_PRIVATE);
        } else if (view.getId() == R.id.protocal) {
            //隐私协议
            startActivity(UserAgreementActivity.class);
        } else if (view.getId() == R.id.backupMnemonic) {
            //备份助记词
//            startActivityForResult(ETHPinCodeActivity.class, KEY_BACKUP);
            Intent intent = new Intent(WalletExportActivity.this, BackUpETHMnemonicActivity.class);
            intent.putExtra("wallet_mnemonic", walletMnemonic);
            startActivity(intent);
        } else if (view.getId() == R.id.saveAction) {
            //修改钱包名
            String newWalletName = mDataBinding.accountDetailsNameField.getContent().getText().toString();
            if (TextUtils.isEmpty(newWalletName)) {
                ToastUtils.showShort("钱包名称不能为空");
                return;
            }
            if (!TextUtils.equals(walletName, newWalletName)) {
                modifyWalletInteract.modifyWalletName(walletId, newWalletName).subscribe(this::modifySuccess);
            } else {
                ToastUtils.showShort("钱包名称不能相同");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEY_PRIVATE) {
            //导出私钥
            if (resultCode == BigDecimal.ONE.intValue()) {
                if (data != null) {
                    String password = data.getStringExtra("input_pwd");
                    modifyWalletInteract.deriveWalletPrivateKey(walletId, password).subscribe(WalletExportActivity.this::showDerivePrivateKeyDialog);
                }
            }
        } else if (requestCode == KEY_PSW_CONFIRM) {
            //修改密码
            if (resultCode == BigDecimal.ONE.intValue()) {
                if (data != null) {
                    String oldPwd = data.getStringExtra("input_old_pwd");
                    String newPwd = data.getStringExtra("input_new_pwd");
                    showLoading(R.string.progress_loading);
                    modifyWalletInteract.modifyWalletPwd(walletId, walletName, oldPwd, newPwd).subscribe(this::modifyPwdSuccess);
                }
            }
        } else if (requestCode == KEY_BACKUP) {
            if (resultCode == BigDecimal.ONE.intValue()) {
                Intent intent = new Intent(WalletExportActivity.this, BackUpETHMnemonicActivity.class);
                intent.putExtra("wallet_mnemonic", walletMnemonic);
                startActivity(intent);
            }
        }
    }

    public void modifyPwdSuccess(ETHWallet ethWallet) {
        dismissLoading();
        ToastUtils.showShort("重置密码成功");
    }


    public void modifySuccess(boolean s) {
        ToastUtils.showShort("保存成功");
    }

    public void showDerivePrivateKeyDialog(String privateKey) {
        if (privateKey.charAt(0) != '0' && privateKey.charAt(1) != 'x') {
            privateKey = "0x".concat(privateKey);
        }
        PrivateKeyDerivetDialog privateKeyDerivetDialog = new PrivateKeyDerivetDialog(this);
        privateKeyDerivetDialog.show();
        privateKeyDerivetDialog.setPrivateKey(privateKey);
    }
}
