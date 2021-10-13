package com.gammaray.ui.activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.LogUtils;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityEthTransDetailLayoutBinding;
import com.gammaray.eth.entity.Ticker;
import com.gammaray.eth.entity.Token;
import com.gammaray.eth.interact.FetchWalletInteract;
import com.gammaray.eth.util.BalanceUtils;
import com.gammaray.eth.viewmodel.ConfirmationViewModel;
import com.gammaray.eth.viewmodel.ConfirmationViewModelFactory;
import com.gammaray.eth.viewmodel.TokensViewModel;
import com.gammaray.eth.viewmodel.TokensViewModelFactory;
import com.gammaray.utils.SharedPreUtils;

import org.greenrobot.eventbus.EventBus;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class ETHTransDetailActivity extends BaseActivity<ActivityEthTransDetailLayoutBinding> {

    private String mGasPrice;

    private String mGasLimit;

    private String mFromAddress;

    private String mToAddress;

    private String mDAppUrl;

    private String mTransValue;

    private TokensViewModelFactory tokensViewModelFactory;

    private TokensViewModel tokensViewModel;

    private FetchWalletInteract fetchWalletInteract;

    private ConfirmationViewModelFactory confirmationViewModelFactory;

    private ConfirmationViewModel viewModel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_eth_trans_detail_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleString = "发送";
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        if (getIntent() != null) {
            mTransValue = getIntent().getStringExtra("trans_value");
            mGasPrice = getIntent().getStringExtra("gas_price");
            mGasLimit = getIntent().getStringExtra("gas_limit");
            mFromAddress = getIntent().getStringExtra("from");
            mToAddress = getIntent().getStringExtra("to");
            mDAppUrl = getIntent().getStringExtra("dapp_url");
        }

        fetchWalletInteract = new FetchWalletInteract();
        tokensViewModelFactory = new TokensViewModelFactory();
        tokensViewModel = ViewModelProviders.of(this, tokensViewModelFactory).get(TokensViewModel.class);
        tokensViewModel.prices().observe(this, this::onPrices);
        tokensViewModel.prepare();

        confirmationViewModelFactory = new ConfirmationViewModelFactory();
        viewModel = ViewModelProviders.of(this, confirmationViewModelFactory)
                .get(ConfirmationViewModel.class);
        viewModel.sendTransaction().observe(this, this::onTransaction);

        mDataBinding.tvTransEth.setText(getString(R.string.eth_trans_value, mTransValue));
        mDataBinding.tvReceiverValue.setText(mFromAddress);
        mDataBinding.tvDAppValue.setText(getString(R.string.eth_dapp_url, mDAppUrl));

        mDataBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = SharedPreUtils.getString(ETHTransDetailActivity.this, SharedPreUtils.KEY_PIN);
                viewModel.createTransaction(password,
                        mToAddress,
                        Convert.toWei(mTransValue, Convert.Unit.ETHER).toBigInteger(),
                        BalanceUtils.gweiToWei(new BigDecimal(mGasPrice)),
                        BalanceUtils.gweiToWei(new BigDecimal(mGasLimit)));
            }
        });
    }

    private void onPrices(List<Ticker.TickersBean> ticker) {
        if (ticker != null && ticker.size() > 0) {
            for (Ticker.TickersBean tickersBean : ticker) {
                if ("ETH".equalsIgnoreCase(tickersBean.getSymbol())) {
                    String curCNYPrice = tickersBean.getPrice();
                    if (!TextUtils.isEmpty(curCNYPrice) && !TextUtils.isEmpty(mTransValue)) {
                        String transValueForCNY = BalanceUtils.ethToUsd(curCNYPrice, mTransValue);
                        String maxTransFeeForCNY = BalanceUtils.ethToUsd(curCNYPrice, mGasLimit);
                        mDataBinding.tvTransEthRmb.setText(getString(R.string.eth_for_cny, transValueForCNY));
                        mDataBinding.tvTransFee.setText(getString(R.string.eth_gas_price, mGasPrice, maxTransFeeForCNY));
                        BigDecimal result = new BigDecimal(transValueForCNY).add(new BigDecimal(maxTransFeeForCNY));
                        mDataBinding.tvMaxTransFee.setText(getString(R.string.eth_for_cny_, result.toPlainString()));
                    }
                }
            }
        }
    }

    private void onTransaction(String hash) {
        Log.e("onTransaction", "Success--" + hash);
    }
}
