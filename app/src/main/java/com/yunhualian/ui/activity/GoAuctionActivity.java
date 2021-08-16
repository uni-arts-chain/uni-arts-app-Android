package com.yunhualian.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.constant.AppConstant;
import com.yunhualian.databinding.ActivityGoAuctionLayoutBinding;
import com.yunhualian.entity.AccountIdVo;
import com.yunhualian.entity.AuctionArtVo;
import com.yunhualian.entity.AuctionVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.entity.StdoutLogger;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.art.AuctionArtDetailActivity;
import com.yunhualian.ui.activity.user.SellArtActivity;
import com.yunhualian.ui.fragment.SendIntegrationTest;
import com.yunhualian.ui.fragment.ToHexV28Kt;
import com.yunhualian.utils.DateFormatUtils;
import com.yunhualian.utils.DateUtil;
import com.yunhualian.utils.SharedPreUtils;
import com.yunhualian.utils.ToastManager;
import com.yunhualian.widget.AuctionDatePicker;

import java.text.ParseException;
import java.util.HashMap;

import jp.co.soramitsu.fearless_utils.encrypt.Signer;
import jp.co.soramitsu.fearless_utils.scale.EncodableStruct;
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService;
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.SubmittableExtrinsicV28;
import retrofit2.Call;
import retrofit2.Response;

public class GoAuctionActivity extends BaseActivity<ActivityGoAuctionLayoutBinding> implements View.OnClickListener {

    private SellingArtVo sellingArtVo;
    private boolean isCanCut; //是否可拆分
    private int remainAmount; //剩余份数
    private int defaultAmount = 1; //初始拍卖份数
    private String receiveAddress = "";
    public SocketService rxWebSocket;
    private AuctionDatePicker mTimerPicker;

    @Override
    public int getLayoutId() {
        return R.layout.activity_go_auction_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.start_auction_;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        initTimerPicker();

        rxWebSocket = new SocketService(new Gson(), new StdoutLogger(), new WebSocketFactory(), i -> 0);
        rxWebSocket.start(AppConstant.RPC);

        if (getIntent() != null) {
            sellingArtVo = (SellingArtVo) getIntent().getSerializableExtra(SellArtActivity.ARTINFO);
        }
        if (sellingArtVo != null) {
            //3代表可拆分
            isCanCut = sellingArtVo.getCollection_mode() == 3;
            remainAmount = sellingArtVo.getHas_amount() - sellingArtVo.getSelling_amount();
            if (TextUtils.isEmpty(sellingArtVo.getPrice())) {
                mDataBinding.edStartAuctionPrice.setText("0");
            } else {
                mDataBinding.edStartAuctionPrice.setText(sellingArtVo.getPrice());
            }
        }

        if (isCanCut && remainAmount > 1) {
            mDataBinding.inputAmount.setEnabled(true);
            mDataBinding.add.setClickable(true);
            mDataBinding.cut.setClickable(true);
        } else {
            mDataBinding.inputAmount.setEnabled(false);
            mDataBinding.add.setClickable(false);
            mDataBinding.cut.setClickable(false);
        }
        mDataBinding.tvStartAuctionTime.setText(DateUtil.dateToStringWithAll(DateUtil.getCurrentTime()));
        mDataBinding.tvEndAuctionTime.setText(DateUtil.dateToStringWithAll(DateUtil.getTomorrowCurrentTime()));
        mDataBinding.tvEndAuctionTime.setOnClickListener(this);
        initListener();
        getAddress();


    }

    private void initTimerPicker() {
        //24 * 60 * 60 * 3 * 1000
        long oneWeekTimeMills = 24 * 60 * 60 * 1000 * 7;
        String beginTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis() + oneWeekTimeMills, true);


        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new AuctionDatePicker(this, new AuctionDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                String selectTime = DateFormatUtils.long2Str(timestamp, true).substring(0, 16) + ":00";

                mDataBinding.tvEndAuctionTime.setText(selectTime);
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }

    private void initListener() {
        mDataBinding.add.setOnClickListener(this);
        mDataBinding.cut.setOnClickListener(this);
        mDataBinding.btnAuction.setOnClickListener(this);
        mDataBinding.inputAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mDataBinding.inputAmount.removeTextChangedListener(this);
                String inputString = editable.toString();
                if (TextUtils.isEmpty(inputString)) {
                    defaultAmount = 1;
                    mDataBinding.inputAmount.setText(String.valueOf(defaultAmount));
                    mDataBinding.inputAmount.setSelection(String.valueOf(defaultAmount).length());
                    return;
                }
                if (Integer.parseInt(editable.toString()) > remainAmount) {
                    defaultAmount = remainAmount;
                    mDataBinding.inputAmount.setText(String.valueOf(defaultAmount));
                } else if (Integer.parseInt(editable.toString()) < 1) {
                    defaultAmount = 1;
                    mDataBinding.inputAmount.setText(String.valueOf(defaultAmount));
                } else {
                    defaultAmount = Integer.parseInt(editable.toString());
                    mDataBinding.inputAmount.setText(String.valueOf(defaultAmount));
                }
                mDataBinding.inputAmount.addTextChangedListener(this);
            }
        });
    }

    private void startAuction() throws ParseException {
        showLoading(R.string.progress_loading);
        HashMap<String, String> param = new HashMap<>();
        param.put("art_id", String.valueOf(sellingArtVo.getId()));
        param.put("amount", String.valueOf(defaultAmount));
        param.put("price", mDataBinding.edStartAuctionPrice.getText().toString());
        param.put("price_increment", mDataBinding.edAuctionUnitPrice.getText().toString());
        param.put("start_time", DateUtil.dateToStamp(mDataBinding.tvStartAuctionTime.getText().toString()));
        param.put("end_time", DateUtil.dateToStamp(mDataBinding.tvEndAuctionTime.getText().toString()));
        param.put("encrpt_extrinsic_message", signStr());
        RequestManager.instance().startAuction(param, new MinerCallback<BaseResponseVo<AuctionArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<AuctionArtVo>> call, Response<BaseResponseVo<AuctionArtVo>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Intent intent = new Intent(GoAuctionActivity.this, AuctionArtDetailActivity.class);
                        intent.putExtra("id", response.body().getBody().getId());
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<AuctionArtVo>> call, Response<BaseResponseVo<AuctionArtVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
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
        return ToHexV28Kt.toHex(sigStr);
    }

    private void getAddress() {
        RequestManager.instance().queryAuctionAccountId(new MinerCallback<BaseResponseVo<AccountIdVo>>() {
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add) {
            if (defaultAmount < remainAmount) {
                defaultAmount++;
                mDataBinding.inputAmount.setText(String.valueOf(defaultAmount));
                mDataBinding.inputAmount.setSelection(String.valueOf(defaultAmount).length());
            }
        } else if (view.getId() == R.id.cut) {
            if (defaultAmount > 1) {
                defaultAmount--;
                mDataBinding.inputAmount.setText(String.valueOf(defaultAmount));
                mDataBinding.inputAmount.setSelection(String.valueOf(defaultAmount).length());
            }
        } else if (view.getId() == R.id.tv_end_auction_time) {
            mTimerPicker.show(mDataBinding.tvEndAuctionTime.getText().toString());
        } else if (view.getId() == R.id.btn_auction) {
            if (TextUtils.isEmpty(mDataBinding.edStartAuctionPrice.getText().toString()) ||
                    TextUtils.isEmpty(mDataBinding.edAuctionUnitPrice.getText().toString())) {
                ToastManager.showShort("请将数据填写完整");
            } else {
                try {
                    startAuction();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
