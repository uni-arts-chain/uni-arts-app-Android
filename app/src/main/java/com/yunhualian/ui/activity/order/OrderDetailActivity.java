package com.yunhualian.ui.activity.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityOrderDetailBinding;
import com.yunhualian.entity.BoughtArtVo;
import com.yunhualian.utils.DateUtil;

public class OrderDetailActivity extends BaseActivity<ActivityOrderDetailBinding> {
    public static final String BOUGHT_KEY = "bought_vo";
    BoughtArtVo boughtArtVo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.order_info_detail;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        boughtArtVo = (BoughtArtVo) getIntent().getSerializableExtra(BOUGHT_KEY);
        if (boughtArtVo != null)
            initPageData();
    }

    private void initPageData() {
        Glide.with(this)
                .load(boughtArtVo.getArt().getImg_main_file1().getUrl())
                .into(mDataBinding.hotPicture);
        mDataBinding.artPrize.setText(boughtArtVo.getTotal_price());
        mDataBinding.orderCreateTime.setText(getString(R.string.order_create_time, DateUtil.dateToStringWith(boughtArtVo.getFinished_at() * 1000)));
        mDataBinding.name.setText(boughtArtVo.getArt().getName());
        mDataBinding.orderInfo.setText(getString(R.string.order_info,
                boughtArtVo.getSn()));
        if (TextUtils.isEmpty(boughtArtVo.getArt().getAuthor().getDisplay_name()))
            mDataBinding.artName.setText("未设置昵称");
        else mDataBinding.artName.setText(boughtArtVo.getArt().getAuthor().getDisplay_name());
        mDataBinding.memo.setText("0".concat("份"));
        mDataBinding.addr.setText(getString(R.string.nft_address, boughtArtVo.getArt().getItem_hash()));

        mDataBinding.copyAction.setOnClickListener(v -> {
            if (TextUtils.isEmpty(boughtArtVo.getSn())) {
                ToastUtils.showLongToast(OrderDetailActivity.this, "未找到订单编号");
                return;
            }
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("", boughtArtVo.getSn());
            cm.setPrimaryClip(mClipData);
            ToastUtils.showLongToast(this, "订单编号已复制");
        });
    }
}