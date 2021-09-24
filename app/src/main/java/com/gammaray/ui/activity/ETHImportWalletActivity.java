package com.gammaray.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityEthImportWalletLayoutBinding;
import com.gammaray.eth.domain.ETHWallet;
import com.gammaray.eth.interact.CreateWalletInteract;
import com.gammaray.eth.util.ETHWalletUtils;
import com.gammaray.ui.activity.wallet.ImportWalletActivity;
import com.gammaray.utils.SharedPreUtils;
import com.gammaray.widget.BasePopupWindow;

import static com.gammaray.ui.activity.wallet.WalletEditActivity.RESUME_CER;
import static com.gammaray.ui.activity.wallet.WalletEditActivity.SET_CER;

/*
 * ETH钱包导入页面
 * */
public class ETHImportWalletActivity extends BaseActivity<ActivityEthImportWalletLayoutBinding> implements View.OnClickListener {

    private String mWalletType;
    //0：助记词，1：私钥 2：KeyStore
    private int mDefaultImportType = 0;
    private PopupWindow mImportPopwindow;
    private View mImportView;
    private Button mMnemonicBtn;
    private Button mPrivateKeyBtn;
    private Button mKeyStoreBtn;
    private CreateWalletInteract createWalletInteract;
    private String ethType = ETHWalletUtils.ETH_JAXX_TYPE;
    private String mWalletName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_eth_import_wallet_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions toolBarOptions = new ToolBarOptions();
        toolBarOptions.titleString = "导入钱包";
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, toolBarOptions);

        if (getIntent() != null) {
            mWalletType = getIntent().getStringExtra("wallet_type");
        }
        createWalletInteract = new CreateWalletInteract();

        mDataBinding.rlImportWay.setOnClickListener(this);
        mDataBinding.btnImport.setOnClickListener(this);
        initData();
        initPopWindow();
    }

    private void initPopWindow() {
        mImportView = LayoutInflater.from(ETHImportWalletActivity.this).inflate(
                R.layout.pop_import_type_layout, null);

        mMnemonicBtn = mImportView.findViewById(R.id.btn_mnemonic);
        mPrivateKeyBtn = mImportView.findViewById(R.id.btn_privateKey);
        mKeyStoreBtn = mImportView.findViewById(R.id.btn_json);
        mMnemonicBtn.setOnClickListener(this);
        mPrivateKeyBtn.setOnClickListener(this);
        mKeyStoreBtn.setOnClickListener(this);
        mImportPopwindow = new BasePopupWindow(this);
        mImportPopwindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mImportPopwindow.setContentView(mImportView);
        mImportPopwindow.setAnimationStyle(R.style.mypopwindow_anim_style);

    }

    public void showPopWindow() {
        mImportPopwindow.showAtLocation(mDataBinding.content, Gravity.BOTTOM, 0, 0);
    }

    private void initData() {
        if (mDefaultImportType == 0) {
            mDataBinding.tvImportWay.setText("助记词");
            mDataBinding.edRawData.setHint("请输入助记词，按空格分隔");
        } else if (mDefaultImportType == 1) {
            mDataBinding.tvImportWay.setText("私钥");
            mDataBinding.edRawData.setHint("请输入私钥");
        } else {
            mDataBinding.tvImportWay.setText("KeyStore");
        }
        if (mDefaultImportType == 0 || mDefaultImportType == 1) {
            mDataBinding.rlWalletJson.setVisibility(View.GONE);
            mDataBinding.rlWalletJsonpwd.setVisibility(View.GONE);
            mDataBinding.rlRawData.setVisibility(View.VISIBLE);
        } else {
            mDataBinding.rlWalletJson.setVisibility(View.VISIBLE);
            mDataBinding.rlWalletJsonpwd.setVisibility(View.VISIBLE);
            mDataBinding.rlRawData.setVisibility(View.GONE);
        }
    }

    private void resetPassWord() {
        mWalletName = mDataBinding.edWalletName.getText().toString();
        if (TextUtils.isEmpty(mWalletName) || mWalletName == null) {
            ToastUtils.showShort("钱包名不能为空");
            return;
        }
        Intent intent = new Intent(ETHImportWalletActivity.this, ETHPinCodeActivity.class);
        intent.putExtra(RESUME_CER, true);
        intent.putExtra(SET_CER, true);
        startActivityForResult(intent, mDefaultImportType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            String mnemonic = mDataBinding.edRawData.getText().toString();
            String walletPwd = SharedPreUtils.getString(this, SharedPreUtils.KEY_PIN);
            createWalletInteract.loadWalletByMnemonic(mWalletName, ethType, mnemonic, walletPwd, false).subscribe(this::loadSuccess, this::onError);
        } else if (requestCode == 1) {
            String privateKey = mDataBinding.edRawData.getText().toString();
            String walletPwd = SharedPreUtils.getString(this, SharedPreUtils.KEY_PIN);
            createWalletInteract.loadWalletByPrivateKey(mWalletName, privateKey, walletPwd, false, "ETH").subscribe(this::loadSuccess, this::onError);
        } else if (requestCode == 2) {
            String keyStore = mDataBinding.edRawData.getText().toString();
            String walletPwd = SharedPreUtils.getString(this, SharedPreUtils.KEY_PIN);
            createWalletInteract.loadWalletByKeystore(mWalletName, keyStore, walletPwd, false, "ETH").subscribe(this::loadSuccess, this::onError);
            ;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_mnemonic) {
            mDefaultImportType = 0;
            mImportPopwindow.dismiss();
            initData();
        } else if (view.getId() == R.id.btn_privateKey) {
            mDefaultImportType = 1;
            mImportPopwindow.dismiss();
            initData();
        } else if (view.getId() == R.id.btn_json) {
            mDefaultImportType = 2;
            mImportPopwindow.dismiss();
            initData();
        } else if (view.getId() == R.id.rl_import_way) {
            showPopWindow();
        } else if (view.getId() == R.id.btn_import) {
            resetPassWord();
        }
    }

    private void loadSuccess(ETHWallet wallet) {
        ToastUtils.showShort("导入钱包成功");
    }

    private void onError(Throwable e) {
        ToastUtils.showShort("导入钱包失败");
    }
}
