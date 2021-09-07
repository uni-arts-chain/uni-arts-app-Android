package com.gammaray.ui.activity;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.databinding.ActivityCertificateApplyBinding;

public class CertificateApplyActivity extends BaseActivity<ActivityCertificateApplyBinding> implements View.OnClickListener {

    public static final int REQUEST_CODE = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_certificate_apply;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mDataBinding.artName.setOnClickListener(this);
        initTitle();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.art_name:
                startActivityForResult(SelectPicActivity.class, REQUEST_CODE);
                break;
        }
    }

    public void initTitle() {
        TextView title = mDataBinding.titleLayout.findViewById(R.id.search_edt);
        title.setText(R.string.apply_certify);
        mDataBinding.titleLayout.findViewById(R.id.layout_back).setOnClickListener(view -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            mDataBinding.artName.setText(data.getStringExtra("result"));
        }
    }
}