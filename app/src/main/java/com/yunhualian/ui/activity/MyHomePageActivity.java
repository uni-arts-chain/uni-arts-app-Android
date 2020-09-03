package com.yunhualian.ui.activity;


import android.graphics.Color;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.yunhualian.R;
import com.yunhualian.adapter.MyHomePageAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.ui.fragment.MyHomePagePicuureSortFragment;

import java.util.Arrays;

public class MyHomePageActivity extends BaseActivity implements MyHomePageAdapter.TabPagerListener {
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TabLayout tabLayout;
    AppBarLayout appBarLayout;
    ViewPager viewPager;
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
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        tabLayout = findViewById(R.id.tabLayout);
        appBarLayout = findViewById(R.id.mAppBarLayout);
        viewPager = findViewById(R.id.viewpager);
        setTitle("我的主页");
        collapsingToolbarLayout.setTitle("我的主页");
        collapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);//设置未收缩状态下的字体颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
        adapter = new MyHomePageAdapter(getSupportFragmentManager(), 4, Arrays.asList(getResources().getStringArray(R.array.my_page_tabs)), this);
        adapter.setListener(this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    @Override
    public Fragment getFragment(int position) {
        return MyHomePagePicuureSortFragment.newInstance();
    }
}