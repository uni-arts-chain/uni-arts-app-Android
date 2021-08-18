package com.yunhualian.ui.activity.user;


import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.yunhualian.R;
import com.yunhualian.adapter.MyHomePageAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.ActivityMyHomePageBinding;
import com.yunhualian.entity.UserVo;
import com.yunhualian.ui.fragment.MyHomePageAuctionFragment;
import com.yunhualian.ui.fragment.MyHomePagePicuureSortFragment;
import com.yunhualian.ui.fragment.MyHomePagePicuureSortSellingFragment;
import com.yunhualian.widget.UploadSuccessPopUpWindow;

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
        mDataBinding.collapsingToolbar.setExpandedTitleColor(Color.BLACK);//设置未收缩状态下的字体颜色
        mDataBinding.collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK);
        adapter = new MyHomePageAdapter(getSupportFragmentManager(), 3, Arrays.asList(getResources().getStringArray(R.array.my_page_tabs)), this);
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
        Glide.with(this)
                .load(userVo.getAvatar().getUrl())
                .apply(new RequestOptions().placeholder(R.mipmap.icon_default_head))
                .into(mDataBinding.mineTitleImg);
        if (TextUtils.isEmpty(userVo.getDisplay_name())) {
            mDataBinding.name.setText(R.string.set_display_name_tips);

        } else
            mDataBinding.name.setText(userVo.getDisplay_name());
        mDataBinding.name.setOnClickListener(v -> startActivity(EditNickNameActivity.class));
        if (TextUtils.isEmpty(userVo.getDesc())) {
            mDataBinding.artDesc.setText(R.string.set_desc_tips);
        } else
            mDataBinding.artDesc.setText(userVo.getDesc());

        mDataBinding.artDesc.setOnClickListener(v -> startActivity(UserDescActivity.class));
        mDataBinding.mineTitleImg.setOnClickListener(v -> startActivity(SettingsActivity.class));
    }

    @Override
    public Fragment getFragment(int position) {
        if (position == 0) {
            return MyHomePagePicuureSortFragment.newInstance(MyHomePagePicuureSortFragment.STATE_ONLINE);
        } else if (position == 1) {
            return MyHomePagePicuureSortSellingFragment.newInstance(MyHomePagePicuureSortSellingFragment.STATE_AUCTION);
        } else {
            return MyHomePageAuctionFragment.newInstance();
        }
    }
}