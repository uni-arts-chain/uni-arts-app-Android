package com.gammaray.ui.activity;

import android.content.Intent;
import android.text.TextUtils;

import androidx.lifecycle.ViewModelProviders;

import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityEthTransDetailLayoutBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.ETHToCNYBean;
import com.gammaray.eth.entity.ConfirmationType;
import com.gammaray.eth.entity.GasSettings;
import com.gammaray.eth.entity.Token;
import com.gammaray.eth.util.BalanceUtils;
import com.gammaray.eth.viewmodel.ConfirmationViewModel;
import com.gammaray.eth.viewmodel.ConfirmationViewModelFactory;
import com.gammaray.eth.viewmodel.TokensViewModel;
import com.gammaray.eth.viewmodel.TokensViewModelFactory;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.utils.SharedPreUtils;
import com.gammaray.utils.ToastManager;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

import retrofit2.Call;
import retrofit2.Response;

public class ETHTransDetailActivity extends BaseActivity<ActivityEthTransDetailLayoutBinding> {

    private String mGasLimit;

    private String mFromAddress;

    private String mToAddress;

    private String mDAppUrl;

    private String mTransValue;

    private String mData;

    private ConfirmationViewModelFactory confirmationViewModelFactory;

    private ConfirmationViewModel viewModel;

    private BigInteger mLatestGasPrice;

    private BigInteger mLatestGasLimit;

    private TokensViewModel tokensViewModel;

    private TokensViewModelFactory tokensViewModelFactory;

    private String mCurrentEthInCny;

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
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions, view -> {
            setResult(3);
            finish();
        });

        if (getIntent() != null) {
            mTransValue = getIntent().getStringExtra("trans_value");
            mGasLimit = getIntent().getStringExtra("gas_limit");
            mFromAddress = getIntent().getStringExtra("from");
            mToAddress = getIntent().getStringExtra("to");
            mDAppUrl = getIntent().getStringExtra("dapp_url");
            mData = getIntent().getStringExtra("data");
            mLatestGasLimit = BalanceUtils.gweiToWei(new BigDecimal(mGasLimit));
        }

        confirmationViewModelFactory = new ConfirmationViewModelFactory();
        viewModel = ViewModelProviders.of(this, confirmationViewModelFactory).get(ConfirmationViewModel.class);
        viewModel.prepare(this, ConfirmationType.ETH, mFromAddress, mToAddress, BalanceUtils.EthToWeiInBigInteger(mTransValue), mGasLimit, mData);
        viewModel.sendTransaction().observe(this, this::onTransaction);
        viewModel.gasSettings().observe(this, this::onGasSettings);

        mDataBinding.tvTransEth.setText(getString(R.string.eth_trans_value, mTransValue));
        mDataBinding.tvReceiverValue.setText(getString(R.string.eth_dapp_url, mFromAddress));
        mDataBinding.tvDAppValue.setText(mDAppUrl);

        mDataBinding.btnConfirm.setOnClickListener(view -> {
            if (mLatestGasLimit.compareTo(new BigInteger("0")) != 0) {
                queryETHBalance();
            }
        });
    }

    private void onGasSettings(GasSettings gasSettings) {
        if (gasSettings != null) {
            if (gasSettings.gasPrice.compareTo(new BigInteger("0")) != 0) {
                //单位为wei
                BigInteger b1 = new BigInteger("100");
                BigInteger b2 = new BigInteger("20");
                mLatestGasPrice = gasSettings.gasPrice;
                BigInteger gasLimit = gasSettings.gasLimit;
                mLatestGasLimit = gasLimit.add(gasLimit.multiply(b2).divide(b1));
                queryCurrentETHInCNY();
            }
        }
    }

    private void onTransaction(String hash) {
        dismissLoading();
        if (!TextUtils.isEmpty(hash)) {
            Intent intent = new Intent();
            intent.putExtra("sign_hash", hash);
            setResult(2, intent);
            finish();
        }
    }

    //更新页面
    private void updatePrices(String currentEthInCny) {
        if (!TextUtils.isEmpty(currentEthInCny) && !TextUtils.isEmpty(mTransValue)) {
            String transValueForCNY = BalanceUtils.ethToUsd(currentEthInCny, mTransValue);
            BigInteger totalFee = mLatestGasPrice.multiply(mLatestGasLimit);
            String maxTransFeeForCNY = BalanceUtils.ethToUsd(currentEthInCny, BalanceUtils.weiToEth(totalFee).toString());
            mDataBinding.tvTransEthRmb.setText(getString(R.string.eth_for_cny, transValueForCNY));
            mDataBinding.tvTransFee.setText(getString(R.string.eth_gas_price, BalanceUtils.weiToGwei(mLatestGasPrice), maxTransFeeForCNY));
            BigDecimal result = new BigDecimal(transValueForCNY).add(new BigDecimal(maxTransFeeForCNY));
            mDataBinding.tvMaxTransFee.setText(getString(R.string.eth_for_cny_, result.toPlainString()));
        }
    }

    //获取最新的ETH对应价格
    private void queryCurrentETHInCNY() {
        showLoading(R.string.progress_loading);
        RequestManager.instance().queryCurrentETHInCNY("eth", new MinerCallback<BaseResponseVo<ETHToCNYBean>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<ETHToCNYBean>> call, Response<BaseResponseVo<ETHToCNYBean>> response) {
                dismissLoading();
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getBody().getPrice() != null) {
                            mCurrentEthInCny = response.body().getBody().getPrice().getEth();
                            updatePrices(mCurrentEthInCny);
                        }
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<ETHToCNYBean>> call, Response<BaseResponseVo<ETHToCNYBean>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    //获取当前ETH余额
    private void queryETHBalance() {
        showLoading(R.string.progress_loading);
        tokensViewModelFactory = new TokensViewModelFactory();
        tokensViewModel = ViewModelProviders.of(this, tokensViewModelFactory).get(TokensViewModel.class);
        tokensViewModel.tokens().observe(this, this::onTokens);
        tokensViewModel.prepare();
    }

    private void onTokens(Token[] tokens) {
        dismissLoading();
        if (tokens.length > 0) {
            String balance = tokens[0].balance;
            BigInteger totalFee = mLatestGasPrice.multiply(mLatestGasLimit);
            BigInteger transValue = BalanceUtils.EthToWeiInBigInteger(mTransValue);
            BigInteger ethBalance = BalanceUtils.EthToWeiInBigInteger(balance);
            if (ethBalance.compareTo(totalFee.add(transValue)) >= 0) {
                sendTransaction();
            } else {
                ToastManager.showShort("余额不足");
            }
        }
    }

    private void sendTransaction() {
        showLoading(R.string.progress_loading);
        String password = SharedPreUtils.getString(ETHTransDetailActivity.this, SharedPreUtils.KEY_PIN);
//        viewModel.createTransaction(password,
//                mToAddress,
//                Convert.toWei(mTransValue, Convert.Unit.ETHER).toBigInteger(),
//                mLatestGasPrice,
//                mLatestGasLimit);
        viewModel.createContractTransaction(mToAddress,
                mLatestGasPrice,
                mLatestGasLimit,
                Convert.toWei(mTransValue, Convert.Unit.ETHER).toBigInteger(),
                mData,
                password);
    }
}
