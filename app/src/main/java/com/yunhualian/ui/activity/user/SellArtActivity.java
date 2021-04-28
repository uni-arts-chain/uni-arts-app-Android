package com.yunhualian.ui.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivitySellArtBinding;

public class SellArtActivity extends BaseActivity<ActivitySellArtBinding> {


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
        mToolBarOptions.titleId = R.string.title_detail;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

    }
}