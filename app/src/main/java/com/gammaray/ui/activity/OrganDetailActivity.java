package com.gammaray.ui.activity;


import android.widget.TextView;

import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.databinding.ActivityOrganDetailBinding;

public class OrganDetailActivity extends BaseActivity<ActivityOrganDetailBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_organ_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initTitle();
    }

    public void initTitle() {
        TextView title = mDataBinding.titleLayout.findViewById(R.id.search_edt);
        title.setText(R.string.organ_detail);
        mDataBinding.titleLayout.findViewById(R.id.layout_back).setOnClickListener(view -> finish());
    }
}