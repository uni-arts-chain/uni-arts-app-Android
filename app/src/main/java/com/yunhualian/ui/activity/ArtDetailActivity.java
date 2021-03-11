package com.yunhualian.ui.activity;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ThreadUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.internal.LazilyParsedNumber;
import com.jelly.mango.Mango;
import com.jelly.mango.MultiplexImage;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.sensetime.liveness.motion.util.ToastUtil;
import com.yunhualian.R;
import com.yunhualian.adapter.PicDetailAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.databinding.ActivityArtDetailBinding;
import com.yunhualian.entity.Presenter;
import com.yunhualian.utils.DialogManager;
import com.yunhualian.utils.ZxingEncodingUtils;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import dagger.internal.DaggerCollections;
import io.reactivex.Observable;
import jp.co.soramitsu.fearless_utils.encrypt.model.Keypair;
import io.reactivex.Single;
import jp.co.soramitsu.common.di.FeatureUtils;
import jp.co.soramitsu.fearless_utils.scale.EncodableStruct;
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder;
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService;
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest;
import jp.co.soramitsu.feature_account_api.domain.model.Node;
import jp.co.soramitsu.feature_wallet_api.di.WalletFeatureApi;
import jp.co.soramitsu.feature_wallet_api.di.WalletFeatureApi2;
import jp.co.soramitsu.feature_wallet_api.domain.model.Fee;
import jp.co.soramitsu.feature_wallet_api.domain.model.Token;
import jp.co.soramitsu.feature_wallet_api.domain.model.Transfer;
import jp.co.soramitsu.feature_wallet_api.domain.model.TransferValidityStatus;
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.response.RuntimeVersion;
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.SignedExtrinsic;
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.SubmittableExtrinsic;
import jp.co.soramitsu.feature_wallet_impl.di.WalletFeatureComponent;
import jp.co.soramitsu.feature_wallet_impl.di.WalletFeatureComponent2;
import jp.co.soramitsu.feature_wallet_impl.domain.WalletInteractorImpl;
import jp.co.soramitsu.feature_wallet_impl.presentation.send.amount.ChooseAmountViewModel;
import jp.co.soramitsu.fearless_utils.wsrpc.DeliveryType;
import jp.co.soramitsu.fearless_utils.wsrpc.recovery.ReconnectStrategy;

import org.bouncycastle.util.encoders.Hex;

import javax.inject.Inject;

import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.extrinsics.TransferRequest;

public class ArtDetailActivity extends BaseActivity<ActivityArtDetailBinding> implements View.OnClickListener {

    private String PUBLIC_KEY = "f65a7d560102f2019da9b9d8993f53f51cc38d50cdff3d0b8e71997d7f911ff1";
    private String PRIVATE_KEY = "ae4093af3c40f2ecc32c14d4dada9628a4a42b28ca1a5b200b89321cbc883182";

    PicDetailAdapter picDetailAdapter;
    int totalAmount;
    List<String> lists = Arrays.asList("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573022417628&di=2c1a1a2b8de14cb8721c761cfdbd189a&imgtype=0&src=http%3A%2F%2Fpic27.nipic.com%2F20130313%2F9252150_092049419327_2.jpg",
            "https://i3.wenshen520.com/25257_0.jpg",
            "https://truth.bahamut.com.tw/s01/201601/88f5d73bb1e77e536bdd3e619bb041aa.JPG",
            "http://i2.bvimg.com/607307/5d1d51c2d25e5c5ct.jpg");
    private int selectPostioton = 0;
    WalletInteractorImpl interactor;
    private SS58Encoder sS58Encoder = new SS58Encoder();
    private String URL = "wss://westend-rpc.polkadot.io";
    SocketService socketService;

    @Inject
    ChooseAmountViewModel chooseAmountViewModel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_art_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
//        List<Integer> lists = Arrays.asList(R.mipmap.shanshui, R.mipmap.shanshui, R.mipmap.shanshui);

//        viewModel = ViewModelProviders.of(this, factory).get(ChooseAmountViewModel.class);
//        viewModel = new ViewModelProvider(this).get(ChooseAmountViewModel.class);
////        viewModel = new ViewModelProvider(this, factory).get(ChooseAmountViewModel.class);
//        socketService = new SocketService(new Gson(), new StdoutLogger(), new WebSocketFactory(), i -> 0);
//        socketService.start(URL);
//        sendIntegrationTest2 = new SendIntegrationTest2();
//        sendIntegrationTest2.setup();
        totalAmount = lists.size();
        mDataBinding.cursorTxt.setText("1/".concat("" + totalAmount));
        mDataBinding.banner.setPages(lists, BannerViewHolder::new);
        mDataBinding.largeAction.setOnClickListener(v -> {
            showBigImg();
        });
        mDataBinding.banner.addPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                selectPostioton = position;
                mDataBinding.cursorTxt.setText(position + 1 + "/" + totalAmount);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mDataBinding.copy.setOnClickListener(v -> {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("", "addr");
                    cm.setPrimaryClip(mClipData);
                }
        );
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.popular));
        picDetailAdapter = new PicDetailAdapter(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mDataBinding.artDeatils.setLayoutManager(linearLayoutManager);
        mDataBinding.artDeatils.setAdapter(picDetailAdapter);
        createQrcode("");
        mDataBinding.buyNow.setOnClickListener(this);

//                FeatureUtils.getFeature1<WalletFeatureComponent2>(
//                requireContext(),
//        WalletFeatureApi::class.java
//        )
//            .chooseAmountComponentFactory()
//                .create(this, "address")
//                .inject(this);
//        WalletFeatureComponent2 walletFeatureComponent = FeatureUtils.getFeature1(this, WalletFeatureApi.class);
//        walletFeatureComponent.chooseAmountComponentFactory().create(this, "").inject(this);
//        FeatureUtils.<WalletFeatureComponent2>getFeature1(this, WalletFeatureApi2.class)
//                .chooseAmountComponentFactory().create(this, "").inject(this);
//
//        MappersTKt.mapNetworkTypeToNetworkModel(this);
//        ChooseAmountViewModel chooseAmountViewModel =
//                new ViewModelProvider(this, null).get(ChooseAmountViewModel.class);
//        chooseAmountViewModel.getTransferFee(new Transfer("14rVH93jroTfgBZF1KLxnvaDYkxiZYgk7ggsJKVa5gFJUMdG", BigDecimal.ZERO, Token.Type.DOT));
//        factory2.inject(this);
//        viewModel.getFeeLiveData().observe(this, this::onSuccess);
//        WalletFeatureComponent walletFeatureComponent =
//                FeatureUtils.<WalletFeatureComponent>getFeature1(this, WalletFeatureApi.class);
//        walletFeatureComponent.chooseAmountComponentFactory().create(this, "").inject(this);

//        MappersTKt.mapNetworkTypeToNetworkModel(this);


        if (chooseAmountViewModel != null) {
            chooseAmountViewModel.getFeeLiveData().observe(this, this::onSuccess);
        }
        BigDecimal total = new BigDecimal("10");
        BigDecimal transferable = new BigDecimal("10.0");

        Transfer transfer = new Transfer("", new BigDecimal("1.0"), Token.Type.DOT);
        BigDecimal fee = new BigDecimal("0.1");
        BigDecimal recipientBalance = new BigDecimal("1.2");
        TransferValidityStatus status = transfer.validityStatus(transferable, total, fee, recipientBalance);

        Keypair keyPair = new Keypair(Hex.decode(PRIVATE_KEY), Hex.decode(PUBLIC_KEY), null);
//        EncodableStruct<SubmittableExtrinsic> submittableExtrinsic = new SendIntegrationTest().generateExtrinsic(keyPair, socketService);
//
//        RuntimeRequest feeRequest = new TransferRequest(submittableExtrinsic);

//        Object result = socketService.executeRequest(feeRequest, DeliveryType.ON_RECONNECT).blockingGet().getResult();
//        Object result = sendIntegrationTest2.shouldperformtransfer();
//        Log.e("status", result.toString());

//        Single.fromCallable(() -> {
//            return sendIntegrationTest2.generateExtrinsic(keyPair);
//        }).subscribe(submittableExtrinsic -> {
//            Log.e("", submittableExtrinsic.toString());
//        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
//        DaggerActivityComponent.builder()
//                .activityModule(new ActivityModule(this))
//                .build()
//                .inject(this);
    }

    public void showUserName(String name) {
        ToastUtil.show(this, name);
    }

    private void onSuccess(Fee fee) {
        ToastUtil.show(this, fee.getFeeAmount().toPlainString());
    }


    private void createQrcode(final String mUrl) {
        if (TextUtils.isEmpty(mUrl)) return;
        showLoading(R.string.progress_loading);
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Bitmap>() {
            @Nullable
            @Override
            public Bitmap doInBackground() throws Throwable {

                return ZxingEncodingUtils.createQRCodeNative(mUrl, 300, 300, null);
            }

            @Override
            public void onSuccess(@Nullable Bitmap result) {
                dismissLoading();
                if (null != result)
//                    mDataBinding.ivQrCode.setImageBitmap(result);
//                mDataBinding.ivQrCode2.setImageBitmap(result);
                    DialogManager.showImgDialog(ArtDetailActivity.this, result, (dialog, which) -> {

                    });
            }

            @Override
            public void onCancel() {
                super.onCancel();
                dismissLoading();
            }

            @Override
            public void onFail(Throwable t) {
                super.onFail(t);
                dismissLoading();
            }
        });
    }

    private void showBigImg() {
        List<MultiplexImage> list = new ArrayList<>();
        for (String url : lists) {
            list.add(new MultiplexImage(url, null, MultiplexImage.ImageType.NORMAL));
        }
        Mango.setImages(list);
        Mango.setPosition(selectPostioton);
        Mango.open(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buy_now:
//                Transfer transfer = new Transfer("14rVH93jroTfgBZF1KLxnvaDYkxiZYgk7ggsJKVa5gFJUMdG", BigDecimal.ZERO, Token.Type.DOT);
//                Optional<Fee> fee = new Optional(interactor.getTransferFee(transfer));
//                ToastUtils.showLong("fee = " + fee.getValue().getFeeAmount());
                Single.fromCallable(() -> {
                    return new Transfer("14rVH93jroTfgBZF1KLxnvaDYkxiZYgk7ggsJKVa5gFJUMdG", BigDecimal.ZERO, Token.Type.DOT);
                }).subscribe(transfer -> {
                    interactor.getTransferFee(transfer);
                });

                break;
        }

    }

    public static class BannerViewHolder implements MZViewHolder<String> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, String data) {
//            mImageView.setImageResource(data);
            Glide.with(context).load(data).into(mImageView);
        }
    }

}