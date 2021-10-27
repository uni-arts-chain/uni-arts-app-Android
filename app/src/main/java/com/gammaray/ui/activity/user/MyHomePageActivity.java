package com.gammaray.ui.activity.user;


import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.gammaray.R;
import com.gammaray.adapter.MyHomePageAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.base.YunApplication;
import com.gammaray.databinding.ActivityMyHomePageBinding;
import com.gammaray.entity.UserVo;
import com.gammaray.ui.fragment.MyHomePageAuctionFragment;
import com.gammaray.ui.fragment.MyHomePagePicuureSortFragment;
import com.gammaray.ui.fragment.MyHomePagePicuureSortSellingFragment;

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
//        if (position == 0) {
//            return MyHomePagePicuureSortFragment.newInstance(MyHomePagePicuureSortFragment.STATE_ONLINE);
//        } else if (position == 1) {
//            return MyHomePagePicuureSortSellingFragment.newInstance(MyHomePagePicuureSortSellingFragment.STATE_AUCTION);
//        } else {
//            return MyHomePageAuctionFragment.newInstance();
//        }
        if (position == 0) {
            return MyHomePagePicuureSortFragment.newInstance(MyHomePagePicuureSortFragment.STATE_ONLINE);
        } else {
            return MyHomePagePicuureSortSellingFragment.newInstance(MyHomePagePicuureSortSellingFragment.STATE_AUCTION);
        }
    }
}