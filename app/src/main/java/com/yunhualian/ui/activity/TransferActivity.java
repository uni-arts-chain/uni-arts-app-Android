package com.yunhualian.ui.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.constant.AppConstant;
import com.yunhualian.databinding.ActivityTransferBinding;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.entity.StdoutLogger;
import com.yunhualian.ui.activity.user.SellArtActivity;
import com.yunhualian.ui.fragment.SendIntegrationTest;
import com.yunhualian.ui.fragment.ToHexKt;
import com.yunhualian.utils.SharedPreUtils;
import com.yunhualian.widget.UploadSuccessPopUpWindow;

import org.bouncycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.soramitsu.fearless_utils.encrypt.Base58;
import jp.co.soramitsu.fearless_utils.encrypt.Signer;
import jp.co.soramitsu.fearless_utils.scale.EncodableStruct;
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder;
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService;
import jp.co.soramitsu.feature_account_api.domain.model.Node;
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.extrinsics.TransferRequest;
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.extrinsics.TransferRequestV28;
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.SubmittableExtrinsic;
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.SubmittableExtrinsicV28;

public class TransferActivity extends BaseActivity<ActivityTransferBinding> {
    SellingArtVo sellingArtVo;
    UploadSuccessPopUpWindow uploadSuccessPopUpWindow;
    public int MAXVALUE = 1000;
    private int MAX_DAYS = 180;
    private int MIN_VALUE = 1;
    private int PSW_REQUEST = 3;
    private int buy_amount = 1;

    private String powers_num = "1";

    public SocketService rxWebSocket;
    String balance;
    SendIntegrationTest sendIntegrationTest;
    int lastAmount;
    Matcher m;
    Pattern p;
    SS58Encoder ss58Encoder;
    public int ACCOUNTID_LENGTH = 32;
    public int ADDRESS_LENGTH = 35;
    public int NETWORK_TYPE = Node.NetworkType.POLKADOT.getRuntimeConfiguration().getAddressByte();

    @Override
    public int getLayoutId() {
        return R.layout.activity_transfer;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        sellingArtVo = (SellingArtVo) getIntent().getSerializableExtra(SellArtActivity.ARTINFO);
        if (sellingArtVo != null) {
            lastAmount = sellingArtVo.getHas_amount() - sellingArtVo.getSelling_amount();
            mDataBinding.lastAmount.setText(getString(R.string.transfer_last_amount, lastAmount));
            MAXVALUE = lastAmount;
        }
        p = Pattern.compile("[\u4e00-\u9fa5]");
        ss58Encoder = new SS58Encoder();
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.art_tansfer;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        mDataBinding.iconScan.setOnClickListener(v -> scan());
        rxWebSocket = new SocketService(new Gson(), new StdoutLogger(), new WebSocketFactory(), i -> 0);
        rxWebSocket.start(AppConstant.RPC);
        sendIntegrationTest = new SendIntegrationTest();
        mDataBinding.transferAction.setOnClickListener(v -> {

            if (TextUtils.isEmpty(mDataBinding.adressInput.getText())) {
                ToastUtils.showShort("请输入转让地址");
                return;
            }
            if (new BigDecimal(balance).compareTo(BigDecimal.ZERO) > 0) {
                String address = mDataBinding.adressInput.getText().toString();
                m = p.matcher(address);
                if (m.find()) {
                    ToastUtils.showShort("地址错误");
                    return;
                }
                Base58 base58 = new Base58();
                byte[] bytes;
                try {
                    bytes = base58.decode(address);
                } catch (Exception e) {
                    ToastUtils.showShort("地址错误");
                    return;
                }
                if (bytes[0] != NETWORK_TYPE) {
                    ToastUtils.showShort("地址错误");
                    return;
                }
                if (bytes.length != ADDRESS_LENGTH) {
                    ToastUtils.showShort("地址错误");
                    return;
                }

                if (ss58Encoder.decode(address).length != ACCOUNTID_LENGTH) {
                    ToastUtils.showShort("地址错误");
                    return;
                }
                startActivityForResult(PinCodeKtActivity.class, PSW_REQUEST);
            } else showPopWindow();

        });
        new getDesc().execute(rxWebSocket);
//        balance = getBalance();
        initListener();
    }

    private void showPopWindow() {
        uploadSuccessPopUpWindow = new UploadSuccessPopUpWindow(TransferActivity.this, new UploadSuccessPopUpWindow.OnBottomTextviewClickListener() {
            @Override
            public void onCancleClick() {
                uploadSuccessPopUpWindow.dismiss();
            }

            @Override
            public void onPerformClick() {
                uploadSuccessPopUpWindow.dismiss();
            }
        });
        uploadSuccessPopUpWindow.setOneKey(true);
        uploadSuccessPopUpWindow.setConfirmText("确定");
        uploadSuccessPopUpWindow.setContent(getString(R.string.balance_zero_tips));
        uploadSuccessPopUpWindow.showAtLocation(mDataBinding.parent, Gravity.CENTER, 0, 0);
    }


    private void transfer(String address) {
        showLoading(getString(R.string.progress_loading));
        SS58Encoder ss58Encoder = new SS58Encoder();
        TransferRequestV28 transferRequest = new TransferRequestV28 (signStr(Hex.toHexString(ss58Encoder.decode(address)), Integer.parseInt(mDataBinding.inputAmount.getText().toString())));
        String result = sendIntegrationTest.transfer(rxWebSocket, transferRequest);
        dismissLoading();
        if (!TextUtils.isEmpty(result)) {
            ToastUtils.showShort("已提交转让申请");
            finish();
        }
    }

    public void scan() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // android 6.0以上需要动态申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, AppConstant.REQ_PERM_CAMERA);
            return;
        }
        // 访问手机存储申请权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // android 6.0以上需要动态申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(this, QrScanActivity.class);
        startActivityForResult(intent, AppConstant.REQ_QR_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == AppConstant.REQ_QR_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == AppConstant.REQ_QR_CODE || requestCode == AppConstant.REQUEST_CODE_SCAN_GALLERY) {
                    //扫描二维码或者扫描相册成功
                    String bundle = data.getStringExtra("scan_result");
                    ToastUtils.showShort("扫描成功");
                    //将扫描出的信息显示出来
                    mDataBinding.adressInput.setText(bundle);
//                DialogManager.showConfirmDialog("温馨提示",scanResult);
                }
            } else {
                ToastUtils.showShort("取消扫描");
            }
        } else if (requestCode == PSW_REQUEST) {

            if (resultCode == BigDecimal.ONE.intValue()) {
                transfer(mDataBinding.adressInput.getText().toString());
            }

        }
    }

    private void initListener() {
        mDataBinding.inputAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputString = s.toString();
                if (TextUtils.isEmpty(inputString)) {
                    mDataBinding.inputAmount.setText(String.valueOf(0));
                    mDataBinding.inputAmount.setSelection(String.valueOf(0).length());
                    return;
                }
                BigDecimal inputAmount = new BigDecimal(inputString);
                BigDecimal minValue = new BigDecimal(MIN_VALUE);
                BigDecimal maxValue = new BigDecimal(MAXVALUE);
                if (inputAmount.compareTo(minValue) < 0) {
                    mDataBinding.inputAmount.setText(minValue.toString());
                    mDataBinding.inputAmount.setSelection(minValue.toString().length());
                } else if (inputAmount.compareTo(maxValue) > 0) {
                    mDataBinding.inputAmount.setText(maxValue.toString());
                    mDataBinding.inputAmount.setSelection(maxValue.toPlainString().length());
                } else if (inputString.length() >= 1 && inputString.substring(0, 1).equals("0") && !inputString.equals("0")) {
                    mDataBinding.inputAmount.setText(inputAmount.toPlainString());
                    mDataBinding.inputAmount.setSelection(inputAmount.toPlainString().length());
                }
                if (TextUtils.isEmpty(mDataBinding.inputAmount.getText().toString())) {
                    buy_amount = MIN_VALUE;
                } else
                    buy_amount = Integer.parseInt(mDataBinding.inputAmount.getText().toString());
                powers_num = String.valueOf(buy_amount);
//                String price = new BigDecimal(orderAmountVo.getPrice()).multiply(new BigDecimal(powers_num)).stripTrailingZeros().toPlainString();
//                mDataBinding.price.setText(getString(R.string.text_buy_amount, price));
//                mDataBinding.priceTotal.setText(getString(R.string.text_buy_amount, price));
            }
        });

        mDataBinding.add.setOnClickListener(v -> {
            buy_amount++;
            mDataBinding.inputAmount.setText("" + buy_amount);
            mDataBinding.inputAmount.setSelection(String.valueOf(buy_amount).length());
        });
        mDataBinding.cut.setOnClickListener(v -> {
            buy_amount--;
            if (buy_amount < 0) {
                buy_amount = 0;
                return;
            }
            mDataBinding.inputAmount.setText("" + buy_amount);
            mDataBinding.inputAmount.setSelection(String.valueOf(buy_amount).length());
        });
    }

    private EncodableStruct<SubmittableExtrinsicV28> signStr(String receiveAddress, int amount) {
        String privateKey = SharedPreUtils.getString(this, SharedPreUtils.KEY_PRIVATE);
        String publicKey = SharedPreUtils.getString(this, SharedPreUtils.KEY_PUBLICKEY);
        String nonce = SharedPreUtils.getString(this, SharedPreUtils.KEY_NONCE);

        Signer signer = new Signer();
        receiveAddress = receiveAddress.contains("0x") ? receiveAddress.substring(2) : receiveAddress;
        EncodableStruct<SubmittableExtrinsicV28> sigStr
                = sendIntegrationTest.shouldfee(
                receiveAddress,
                amount,
                sellingArtVo.getCollection_id(),
                sellingArtVo.getItem_id(), privateKey, publicKey, nonce.substring(2), rxWebSocket, signer, AppConstant.genesisHash);
//        String hexStr = ToHexKt.toHex(sigStr);
        return sigStr;
    }

    public class getDesc extends AsyncTask<SocketService, Integer, String> {
        @Override
        protected String doInBackground(SocketService... socketServices) {
            SendIntegrationTest sendIntegrationTest = new SendIntegrationTest();
            String balance = sendIntegrationTest.getBalance(socketServices[0], SharedPreUtils.getString(TransferActivity.this, SharedPreUtils.KEY_ADDRESS));
            String balanceStr;
            if (TextUtils.isEmpty(balance)) {
                balanceStr = "0";
            } else {
                balanceStr = new BigDecimal(balance).setScale(4, RoundingMode.DOWN).stripTrailingZeros().toPlainString();
            }
            return balanceStr;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            mBinding.mineCount.setText(getString(R.string.mine_acount, s));
            balance = s;
        }
    }
}