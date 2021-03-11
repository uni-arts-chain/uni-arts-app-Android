package com.yunhualian.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.TextWidthColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.upbest.arouter.ArouterModelPath;
import com.yunhualian.R;
import com.yunhualian.adapter.DeriveKeystorePageFragmentAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.databinding.ActivityImportWalletBinding;
import com.yunhualian.ui.fragment.ExportKeystoreQRCodeFragment;
import com.yunhualian.ui.fragment.ExportKeystoreStringFragment;
import com.yunhualian.utils.UUi;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Tiny 熊 @ Upchain.pro
 * WeiXin: xlbxiong
 */
@Route(path = ArouterModelPath.QC_CODE_ACTIVITY)
public class ExportKeystoreActivity extends BaseActivity<ActivityImportWalletBinding> {

    private List<BaseFragment> fragmentList = new ArrayList<>();
    private IndicatorViewPager indicatorViewPager;

    private DeriveKeystorePageFragmentAdapter deriveKeystorePageFragmentAdapter;

    private ExportKeystoreStringFragment deriveKeystoreStringFragment;
    private ExportKeystoreQRCodeFragment deriveKeystoreQRCodeFragment;
    private String privateKey;

    @Override
    public int getLayoutId() {
        return R.layout.activity_import_wallet;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initToolBar();
        initDatas();
        configViews();
    }

    public void initToolBar() {
        mDataBinding.tvTitle.setText(R.string.derive_keystore_title);
    }

    public void initDatas() {
        privateKey = getIntent().getExtras().getString("json");
        deriveKeystoreStringFragment = new ExportKeystoreStringFragment();
        deriveKeystoreQRCodeFragment = new ExportKeystoreQRCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("walletKeystore", privateKey);//这里的values就是我们要传的值
        deriveKeystoreStringFragment.setArguments(bundle);
        deriveKeystoreQRCodeFragment.setArguments(bundle);
        fragmentList.add(deriveKeystoreStringFragment);
        fragmentList.add(deriveKeystoreQRCodeFragment);
    }

    public void configViews() {
        mDataBinding.indicatorView.setSplitAuto(true);
        mDataBinding.indicatorView.setOnTransitionListener(new OnTransitionTextListener()
                .setColor(getResources().getColor(R.color.transfer_advanced_setting_help_text_color), getResources().getColor(R.color.discovery_application_item_name_color))
                .setSize(14, 14));
        mDataBinding.indicatorView.setScrollBar(new TextWidthColorBar(this, mDataBinding.indicatorView, getResources().getColor(R.color.transfer_advanced_setting_help_text_color), UUi.dip2px(2)));
        mDataBinding.indicatorView.setScrollBarSize(50);
        indicatorViewPager = new IndicatorViewPager(mDataBinding.indicatorView, mDataBinding.vpLoadWallet);
        deriveKeystorePageFragmentAdapter = new DeriveKeystorePageFragmentAdapter(this, getSupportFragmentManager(), fragmentList);
        indicatorViewPager.setAdapter(deriveKeystorePageFragmentAdapter);
        indicatorViewPager.setCurrentItem(0, false);
        mDataBinding.vpLoadWallet.setOffscreenPageLimit(4);
    }

}
