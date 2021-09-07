package com.gammaray.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.constant.AppConstant;
import com.gammaray.databinding.ActivityGoAuctionLayoutBinding;
import com.gammaray.entity.AccountIdVo;
import com.gammaray.entity.AuctionArtVo;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.SellingArtVo;
import com.gammaray.entity.StdoutLogger;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.ui.activity.art.AuctionArtDetailActivity;
import com.gammaray.ui.activity.user.SellArtActivity;
import com.gammaray.ui.fragment.SendIntegrationTest;
import com.gammaray.ui.fragment.ToHexV28Kt;
import com.gammaray.utils.DateFormatUtils;
import com.gammaray.utils.DateUtil;
import com.gammaray.utils.SharedPreUtils;
import com.gammaray.utils.ToastManager;
import com.gammaray.widget.AuctionDatePicker;
import com.gammaray.widget.BasePopupWindow;

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
    private AuctionDatePicker mStartTimePicker;
    private AuctionDatePicker mEndTimePicker;
    private long mDefaultEndTimeMills;
    private String passwd;
    private PopupWindow mPasswordPopwindow;

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

        initStartTimer();
        initEndTimer();

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
            if (!isCanCut) {
                mDataBinding.add.setVisibility(View.INVISIBLE);
                mDataBinding.cut.setVisibility(View.INVISIBLE);
                mDataBinding.add.setClickable(false);
                mDataBinding.cut.setClickable(false);
            } else {
                mDataBinding.add.setVisibility(View.VISIBLE);
                mDataBinding.cut.setVisibility(View.VISIBLE);
                mDataBinding.add.setClickable(false);
                mDataBinding.cut.setClickable(false);
            }
            mDataBinding.inputAmount.setEnabled(false);
        }
        mDataBinding.tvStartAuctionTime.setText(DateUtil.dateToStringWithAll(DateUtil.getCurrentTime()));
        mDataBinding.tvStartAuctionTime.setOnClickListener(this);
        mDataBinding.tvEndAuctionTime.setText(DateUtil.dateToStringWithAll(DateUtil.getTomorrowCurrentTime()));
        mDataBinding.tvEndAuctionTime.setOnClickListener(this);
        initListener();
        initPasswordPopwindow();
        getAddress();
    }

    private void initStartTimer() {

        String beginTime = DateFormatUtils.long2Str(DateUtil.getTomorrowCurrentTime(), true);
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis() + 315360000000L, true);
        mStartTimePicker = new AuctionDatePicker(this, new AuctionDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                String selectTime = DateFormatUtils.long2Str(timestamp, true).substring(0, 16) + ":00";
                mDefaultEndTimeMills = timestamp + 86400000;
                String defaultEndTime = DateFormatUtils.long2Str(mDefaultEndTimeMills, true).substring(0, 16) + ":00";
                mDataBinding.tvStartAuctionTime.setText(selectTime);
                mDataBinding.tvEndAuctionTime.setText(defaultEndTime);
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mStartTimePicker.setCancelable(true);
        // 显示时和分
        mStartTimePicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mStartTimePicker.setScrollLoop(true);
        // 允许滚动动画
        mStartTimePicker.setCanShowAnim(true);
    }

    private void initEndTimer() {
        //24 * 60 * 60 * 3 * 1000
        long oneWeekTimeMills = 24 * 60 * 60 * 1000 * 6;
        String beginTime;
        String endTime;
        if (mDefaultEndTimeMills == 0) {
            beginTime = DateFormatUtils.long2Str(DateUtil.getTomorrowCurrentTime(), true);
            endTime = DateFormatUtils.long2Str(DateUtil.getTomorrowCurrentTime() + oneWeekTimeMills, true);
        } else {
            beginTime = DateFormatUtils.long2Str(mDefaultEndTimeMills, true);
            endTime = DateFormatUtils.long2Str(mDefaultEndTimeMills + oneWeekTimeMills, true);
        }


        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mEndTimePicker = new AuctionDatePicker(this, new AuctionDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                String selectTime = DateFormatUtils.long2Str(timestamp, true).substring(0, 16) + ":00";

                mDataBinding.tvEndAuctionTime.setText(selectTime);
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mEndTimePicker.setCancelable(true);
        // 显示时和分
        mEndTimePicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mEndTimePicker.setScrollLoop(true);
        // 允许滚动动画
        mEndTimePicker.setCanShowAnim(true);
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
        int amount = 0;
        try {
            amount = Integer.parseInt(mDataBinding.inputAmount.getText().toString());
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

    private void initPasswordPopwindow() {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.pop_passwd_layout, null);
        ImageView closeBtn = contentView.findViewById(R.id.img_close);
        TextView confirmBtn = contentView.findViewById(R.id.confirm);
        TextView passwdHintTv = contentView.findViewById(R.id.tv_passwd_len_hint);
        EditText passwdEd = contentView.findViewById(R.id.ed_password);
        mPasswordPopwindow = new BasePopupWindow(this);
        mPasswordPopwindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPasswordPopwindow.setContentView(contentView);
        mPasswordPopwindow.setOutsideTouchable(false);
        mPasswordPopwindow.setTouchable(true);
        mPasswordPopwindow.setAnimationStyle(R.style.mypopwindow_anim_style);

        passwdEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence.toString())) {
                    passwdHintTv.setText("0/6");
                } else {
                    passwdHintTv.setText(charSequence.toString().length() + "/6");
                    passwd = charSequence.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwd.equals(SharedPreUtils.getString(GoAuctionActivity.this, SharedPreUtils.KEY_PIN))) {
                    try {
                        startAuction();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showShort("密码错误");
                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwdEd.setText("");
                mPasswordPopwindow.dismiss();
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
        } else if (view.getId() == R.id.tv_start_auction_time) {
            initStartTimer();
            mStartTimePicker.show(mDataBinding.tvStartAuctionTime.getText().toString());
        } else if (view.getId() == R.id.tv_end_auction_time) {
            initEndTimer();
            mEndTimePicker.show(mDataBinding.tvEndAuctionTime.getText().toString());
        } else if (view.getId() == R.id.btn_auction) {
            if (TextUtils.isEmpty(mDataBinding.edStartAuctionPrice.getText().toString()) ||
                    TextUtils.isEmpty(mDataBinding.edAuctionUnitPrice.getText().toString())) {
                ToastManager.showShort("请将数据填写完整");
            } else {
                mPasswordPopwindow.showAtLocation(mDataBinding.parentLayout, Gravity.CENTER, 0, 0);
            }
        }
    }
}
