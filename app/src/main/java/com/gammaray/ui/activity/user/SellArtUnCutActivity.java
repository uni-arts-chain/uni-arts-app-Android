package com.gammaray.ui.activity.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.base.YunApplication;
import com.gammaray.constant.AppConstant;
import com.gammaray.databinding.ActivitySellArtUnCutBinding;
import com.gammaray.entity.AccountIdVo;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.SellingArtVo;
import com.gammaray.entity.StdoutLogger;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.ui.activity.PinCodeKtActivity;
import com.gammaray.ui.fragment.SendIntegrationTest;
import com.gammaray.ui.fragment.ToHexV28Kt;
import com.gammaray.utils.SharedPreUtils;
import com.gammaray.widget.UploadSuccessPopUpWindow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import jp.co.soramitsu.fearless_utils.encrypt.Signer;
import jp.co.soramitsu.fearless_utils.scale.EncodableStruct;
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService;
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.SubmittableExtrinsicV28;
import retrofit2.Call;
import retrofit2.Response;

public class SellArtUnCutActivity extends BaseActivity<ActivitySellArtUnCutBinding> {
    public static final String ARTINFO = "art";
    SellingArtVo sellingArtVo;
    String DEFAULT_PRICE = "0.0";
    public SocketService rxWebSocket;
    private String receiveAddress = "";

    UploadSuccessPopUpWindow uploadSuccessPopUpWindow;
    String balance;
    boolean isFromDetail;
    @Override
    public int getLayoutId() {
        return R.layout.activity_sell_art_un_cut;
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
        isFromDetail = getIntent().getBooleanExtra("is_from_detail",false);
        if (sellingArtVo != null) {
            initPageData();
        }

        rxWebSocket = new SocketService(new Gson(), new StdoutLogger(), new WebSocketFactory(), i -> 0);
        rxWebSocket.start(AppConstant.RPC);
        mDataBinding.sellAction.setOnClickListener(v -> startActivityForResult(PinCodeKtActivity.class, 0));
        new getDesc().execute(rxWebSocket);
//        balance = getBalance();
        getAddress();
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


    public class getDesc extends AsyncTask<SocketService, Integer, String> {
        @Override
        protected String doInBackground(SocketService... socketServices) {
            SendIntegrationTest sendIntegrationTest = new SendIntegrationTest();
            String balance = sendIntegrationTest.getBalance(socketServices[0], SharedPreUtils.getString(SellArtUnCutActivity.this, SharedPreUtils.KEY_ADDRESS));
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

    private void initPageData() {
        if (TextUtils.isEmpty(sellingArtVo.getPrice()))
            mDataBinding.price.setText(YunApplication.PAY_CURRENCY.concat(DEFAULT_PRICE));
        else
            mDataBinding.price.setText(YunApplication.PAY_CURRENCY.concat(sellingArtVo.getPrice()));
    }

    private String signStr() {
        String privateKey = SharedPreUtils.getString(this, SharedPreUtils.KEY_PRIVATE);
        String publicKey = SharedPreUtils.getString(this, SharedPreUtils.KEY_PUBLICKEY);
        String nonce = SharedPreUtils.getString(this, SharedPreUtils.KEY_NONCE);

        Signer signer = new Signer();
        SendIntegrationTest sendIntegrationTest = new SendIntegrationTest();
        EncodableStruct<SubmittableExtrinsicV28> sigStr
                = sendIntegrationTest.shouldfee(
                receiveAddress.substring(2),
                1,
                sellingArtVo.getCollection_id(),
                sellingArtVo.getItem_id(), privateKey, publicKey, nonce.substring(2), rxWebSocket, signer, AppConstant.genesisHash);
        String hexStr = ToHexV28Kt.toHex(sigStr);
        return hexStr;
    }

    private void sellArt() {
        String price = mDataBinding.cutPriceInput.getText().toString();
        if (TextUtils.isEmpty(price) || price.equals("0")) {
            ToastUtils.showShort(getString(R.string.text_input_amount_null_tips));
            return;
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("art_id", String.valueOf(sellingArtVo.getId()));
        param.put("amount", String.valueOf(1));
        param.put("price", price);
        param.put("currency", "rmb");
        param.put("encrpt_extrinsic_message", signStr());
        RequestManager.instance().sellArt(param, new MinerCallback<BaseResponseVo<SellingArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {
                if (response.isSuccessful()) {
                    ToastUtils.showShort("挂单成功");
//                    if(isFromDetail){
//                        EventBus.getDefault().postSticky(new EventBusMessageEvent(ExtraConstant.EVENT_SELL_SUCCESS_FROM_DETAIL, null));
//                    }else{
//                        EventBus.getDefault().postSticky(new EventBusMessageEvent(ExtraConstant.EVENT_SELL_SUCCESS, null));
//                    }
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
                    Log.e("tag","address---" + receiveAddress);
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