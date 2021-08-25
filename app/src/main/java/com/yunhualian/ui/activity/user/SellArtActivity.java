package com.yunhualian.ui.activity.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.constant.AppConstant;
import com.yunhualian.constant.ExtraConstant;
import com.yunhualian.databinding.ActivitySellArtBinding;
import com.yunhualian.entity.AccountIdVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.EventBusMessageEvent;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.entity.StdoutLogger;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.PinCodeKtActivity;
import com.yunhualian.ui.fragment.SendIntegrationTest;
import com.yunhualian.ui.fragment.ToHexKt;
import com.yunhualian.ui.fragment.ToHexV28Kt;
import com.yunhualian.utils.SharedPreUtils;
import com.yunhualian.widget.UploadSuccessPopUpWindow;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import jp.co.soramitsu.fearless_utils.encrypt.Signer;
import jp.co.soramitsu.fearless_utils.scale.EncodableStruct;
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService;
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.SubmittableExtrinsicV28;
import retrofit2.Call;
import retrofit2.Response;

public class SellArtActivity extends BaseActivity<ActivitySellArtBinding> {
    public static final String ARTINFO = "art";
    SellingArtVo sellingArtVo;

    UploadSuccessPopUpWindow uploadSuccessPopUpWindow;
    public SocketService rxWebSocket;
    private String receiveAddress = "";

    String balance;
    boolean isFromDetail;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sell_art;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.title_sell;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        sellingArtVo = (SellingArtVo) getIntent().getSerializableExtra(ARTINFO);
        isFromDetail = getIntent().getBooleanExtra("is_from_detail", false);
        if (sellingArtVo != null) {
            initPageData();
        }

        rxWebSocket = new SocketService(new Gson(), new StdoutLogger(), new WebSocketFactory(), i -> 0);
        rxWebSocket.start(AppConstant.RPC);
        mDataBinding.sellAction.setOnClickListener(v -> {

            startActivityForResult(PinCodeKtActivity.class, 0);

        });
        new getDesc().execute(rxWebSocket);
//        balance = getBalance();
        getAddress();
        mDataBinding.cutPriceInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 2 + 1);
                        mDataBinding.cutPriceInput.setText(s);
                        mDataBinding.cutPriceInput.setSelection(s.length()); //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    mDataBinding.cutPriceInput.setText(s);
                    mDataBinding.cutPriceInput.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mDataBinding.cutPriceInput.setText(s.subSequence(0, 1));
                        mDataBinding.cutPriceInput.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDataBinding.amountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //删除“.”后面超过2位后的数据

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) return;
                if (new BigDecimal(s.toString()).compareTo(new BigDecimal(sellingArtVo.getHas_amount())) > 0) {
                    mDataBinding.amountInput.setText(String.valueOf(sellingArtVo.getHas_amount()));
                }
            }
        });
    }

    public class getDesc extends AsyncTask<SocketService, Integer, String> {
        @Override
        protected String doInBackground(SocketService... socketServices) {
            SendIntegrationTest sendIntegrationTest = new SendIntegrationTest();
            String balance = sendIntegrationTest.getBalance(socketServices[0], SharedPreUtils.getString(SellArtActivity.this, SharedPreUtils.KEY_ADDRESS));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BigDecimal.ONE.intValue()) {

            if (new BigDecimal(balance).compareTo(BigDecimal.ZERO) > 0)
                sellArt();
            else showPopWindow();

        }
    }

    private void showPopWindow() {
        uploadSuccessPopUpWindow = new UploadSuccessPopUpWindow(this, new UploadSuccessPopUpWindow.OnBottomTextviewClickListener() {
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

    private void initPageData() {
        mDataBinding.artBalance.setText(String.valueOf(sellingArtVo.getHas_amount() - sellingArtVo.getSelling_amount()).concat("份"));
//        mDataBinding.amountInput.setText(String.valueOf(sellingArtVo.getHas_amount()));
//        mDataBinding.cutPriceInput.setText(sellingArtVo.getPrice());
    }

    private String signStr() {
        int amount = 0;
        try {
            amount = Integer.parseInt(mDataBinding.amountInput.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String privateKey = SharedPreUtils.getString(this, SharedPreUtils.KEY_PRIVATE);
        String publicKey = SharedPreUtils.getString(this, SharedPreUtils.KEY_PUBLICKEY);
        String nonce = SharedPreUtils.getString(this, SharedPreUtils.KEY_NONCE);

        Signer signer = new Signer();
        SendIntegrationTest sendIntegrationTest = new SendIntegrationTest();
        EncodableStruct<SubmittableExtrinsicV28> sigStr
                = sendIntegrationTest.shouldfee(
                receiveAddress.substring(2),
                amount,
                sellingArtVo.getCollection_id(),
                sellingArtVo.getItem_id(), privateKey, publicKey, nonce.substring(2), rxWebSocket, signer, AppConstant.genesisHash);
        String hexStr = ToHexV28Kt.toHex(sigStr);
        LogUtils.e("str == " + hexStr);
        return hexStr;
    }

    private void sellArt() {
        if (TextUtils.isEmpty(mDataBinding.amountInput.getText().toString()) || TextUtils.equals("0", mDataBinding.amountInput.getText().toString())) {
            ToastUtils.showShort(getString(R.string.text_sell_amount_null));
            return;
        }
        if (TextUtils.isEmpty(mDataBinding.cutPriceInput.getText().toString()) || TextUtils.equals("0", mDataBinding.cutPriceInput.getText().toString())) {
            ToastUtils.showShort(getString(R.string.text_sell_cut_price_tips));
            return;
        }
        showLoading(getString(R.string.progress_loading));
        HashMap<String, String> param = new HashMap<>();
        param.put("art_id", String.valueOf(sellingArtVo.getId()));
        param.put("amount", mDataBinding.amountInput.getText().toString());
        param.put("price", mDataBinding.cutPriceInput.getText().toString());
        param.put("currency", "rmb");
        param.put("encrpt_extrinsic_message", signStr());
        RequestManager.instance().sellArt(param, new MinerCallback<BaseResponseVo<SellingArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    ToastUtils.showShort("挂单成功");
                    finish();
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    private void getAddress() {
        RequestManager.instance().queryAccountId(new MinerCallback<BaseResponseVo<AccountIdVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<AccountIdVo>> call, Response<BaseResponseVo<AccountIdVo>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getBody() != null)
                        receiveAddress = response.body().getBody().getLock_account_id();
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<AccountIdVo>> call, Response<BaseResponseVo<AccountIdVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

}