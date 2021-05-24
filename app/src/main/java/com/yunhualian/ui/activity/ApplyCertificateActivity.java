package com.yunhualian.ui.activity;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yunhualian.R;
import com.yunhualian.adapter.MineCertifyArtsAdapter;
import com.yunhualian.adapter.SignCompanysAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.databinding.ActivityApplyCertificateBinding;
import com.yunhualian.ui.activity.art.ArtDetailActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplyCertificateActivity extends BaseActivity<ActivityApplyCertificateBinding>
        implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    MineCertifyArtsAdapter mineCertifyArtsAdapter;
    SignCompanysAdapter signCompanysAdapter;
    List sorts;

    @Override
    public int getLayoutId() {
        return R.layout.activity_apply_certificate;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initTitle();
        mDataBinding.showMore.setOnClickListener(this);
        List<String> sort = Arrays.asList(getResources().getStringArray(R.array.my_sign));
        sorts = new ArrayList(sort);
        mineCertifyArtsAdapter = new MineCertifyArtsAdapter(sorts);
        signCompanysAdapter = new SignCompanysAdapter(sorts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        mDataBinding.mySignList.setLayoutManager(linearLayoutManager);
        mDataBinding.mySignList.setAdapter(mineCertifyArtsAdapter);
        mineCertifyArtsAdapter.setOnItemClickListener(this);
        mDataBinding.companys.setLayoutManager(linearLayoutManager2);
        mDataBinding.companys.setAdapter(signCompanysAdapter);
        mDataBinding.goCertify.setOnClickListener(this);
        signCompanysAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(OrganDetailActivity.class);
            }
        });
    }

    public void initTitle() {
        TextView title = mDataBinding.titleLayout.findViewById(R.id.search_edt);
        title.setText(R.string.apply_certify);
        mDataBinding.titleLayout.findViewById(R.id.layout_back).setOnClickListener(view -> finish());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_more:
                List<String> strings = Arrays.asList(getResources().getStringArray(R.array.popular));
                List sttins = new ArrayList(strings);
                sorts.addAll(sttins);
                mineCertifyArtsAdapter.notifyDataSetChanged();
                break;
            case R.id.go_certify:
                startActivity(CertificateApplyActivity.class);
                break;
        }

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//        startActivity(ArtDetailActivity.class);
    }
}