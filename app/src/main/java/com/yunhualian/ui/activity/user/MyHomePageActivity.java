package com.yunhualian.ui.activity.user;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.yunhualian.R;
import com.yunhualian.adapter.MyHomePageAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.ActivityMyHomePageBinding;
import com.yunhualian.entity.UserVo;
import com.yunhualian.ui.activity.UploadArtActivity;
import com.yunhualian.ui.fragment.MyHomePagePicuureSortFragment;

import java.util.Arrays;

public class MyHomePageActivity extends BaseActivity<ActivityMyHomePageBinding> implements MyHomePageAdapter.TabPagerListener {
    MyHomePageAdapter adapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_my_home_page;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.my_page;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
//        mDataBinding.collapsingToolbar.setTitle("我的主页");
        mDataBinding.collapsingToolbar.setExpandedTitleColor(Color.BLACK);//设置未收缩状态下的字体颜色
        mDataBinding.collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK);
        adapter = new MyHomePageAdapter(getSupportFragmentManager(), 2, Arrays.asList(getResources().getStringArray(R.array.my_page_tabs)), this);
        adapter.setListener(this);
        mDataBinding.viewpager.setAdapter(adapter);
        mDataBinding.tabLayout.setupWithViewPager(mDataBinding.viewpager);
        mDataBinding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
        mDataBinding.uploadImage.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString(UploadArtActivity.FROM, "home");
                    startActivity(UploadArtActivity.class);
                }

        );
//        mDataBinding.back.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPageData();
    }

    public void initPageData() {
        if (YunApplication.getmUserVo() == null) return;
        UserVo userVo = YunApplication.getmUserVo();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.icon_default_head);
        Glide.with(this).load(userVo.getAvatar().getUrl()).apply(requestOptions).into(mDataBinding.mineTitleImg);
        if (TextUtils.isEmpty(userVo.getDisplay_name())) {
            mDataBinding.name.setText(R.string.set_display_name_tips);
            mDataBinding.name.setOnClickListener(v -> startActivity(EditNickNameActivity.class));
        } else
            mDataBinding.name.setText(userVo.getDisplay_name());
        if (TextUtils.isEmpty(userVo.getArtist_desc())) {
            mDataBinding.artDesc.setText(R.string.set_desc_tips);
            mDataBinding.artDesc.setOnClickListener(v -> startActivity(UserDescActivity.class));
        } else
            mDataBinding.artDesc.setText(userVo.getDesc());
    }

    @Override
    public Fragment getFragment(int position) {
        if (position == 0)
            return MyHomePagePicuureSortFragment.newInstance(MyHomePagePicuureSortFragment.STATE_ONLINE);
        else
            return MyHomePagePicuureSortFragment.newInstance(MyHomePagePicuureSortFragment.STATE_AUCTION);
    }
}