package com.yunhualian.ui.activity;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yunhualian.R;
import com.yunhualian.adapter.SelectPicAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.databinding.ActivitySelectPicBinding;

import java.util.Arrays;
import java.util.List;

public class SelectPicActivity extends BaseActivity<ActivitySelectPicBinding> implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    SelectPicAdapter selectPicAdapter;
    List<String> list;
    String pictureName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_pic;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initTitle();
        initLists();
    }

    public void initLists() {
        list = Arrays.asList(getResources().getStringArray(R.array.popular));
        selectPicAdapter = new SelectPicAdapter(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mDataBinding.picList.setLayoutManager(linearLayoutManager);
        mDataBinding.picList.setAdapter(selectPicAdapter);
        selectPicAdapter.setOnItemClickListener(this);
    }

    public void initTitle() {
        TextView title = mDataBinding.titleLayout.findViewById(R.id.search_edt);
        title.setText(R.string.select_pics);
        mDataBinding.titleLayout.findViewById(R.id.layout_back).setOnClickListener(view -> finish());
        TextView tightText = mDataBinding.titleLayout.findViewById(R.id.txt_right);
        tightText.setTextColor(getResources().getColor(R.color.bg_news_blue));
        tightText.setText(R.string.determine);
        tightText.setVisibility(View.VISIBLE);
        tightText.setOnClickListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        pictureName = list.get(position);
        selectPicAdapter.setClickPosition(position);
        selectPicAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_right:
                if (pictureName != null) {
                    Intent i = new Intent();
                    i.putExtra("result", pictureName);
                    setResult(1, i);
                    finish();
                } else ToastUtils.showLong(getString(R.string.confirm_tips));
                break;
        }
    }
}