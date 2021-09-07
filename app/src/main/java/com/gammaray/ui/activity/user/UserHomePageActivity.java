package com.gammaray.ui.activity.user;


import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.gammaray.R;
import com.gammaray.adapter.MyHomePageAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityUserHomePageBinding;
import com.gammaray.entity.AuctionArtVo;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.MemberInfo;
import com.gammaray.entity.SellingArtVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.ui.fragment.HomeAuctionFragment;
import com.gammaray.ui.fragment.HomeHotFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class UserHomePageActivity extends BaseActivity<ActivityUserHomePageBinding> implements MyHomePageAdapter.TabPagerListener {
    public static final String UID = "uid";
    int uid;
    MemberInfo memberInfo;
    private List<SellingArtVo> popularList = new ArrayList<>();
    private List<AuctionArtVo> auctionList = new ArrayList<>();
    private HomeHotFragment homeHotFragment;
    private HomeAuctionFragment homeAuctionFragment;
    private MyHomePageAdapter pageAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_user_home_page;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.text_user_home_page;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        uid = getIntent().getIntExtra(UID, 0);
        initFragments();
        getPopular(String.valueOf(uid));
        getAuctions(String.valueOf(uid));
        getMemberInfo(String.valueOf(uid));
    }

    private void initFragments() {
        homeHotFragment = new HomeHotFragment(popularList);
        homeAuctionFragment = new HomeAuctionFragment(auctionList);
        pageAdapter = new MyHomePageAdapter(getSupportFragmentManager(), 2, Arrays.asList(getResources().getStringArray(R.array.user_tabs)), this);
        pageAdapter.setListener(this);
        mDataBinding.vpSellAuction.setAdapter(pageAdapter);
        mDataBinding.tabLayout.setupWithViewPager(mDataBinding.vpSellAuction);
        mDataBinding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    public void initPageData() {
        if (null == memberInfo) return;
        if (TextUtils.isEmpty(memberInfo.getDisplay_name())) {
            mDataBinding.name.setText(getString(R.string.no_display_name));
        } else
            mDataBinding.name.setText(memberInfo.getDisplay_name());
        RequestOptions requestOptions = new RequestOptions().placeholder(R.mipmap.icon_default_head);
        Glide.with(this).load(memberInfo.getAvatar().getUrl()).apply(requestOptions).into(mDataBinding.mineTitleImg);

        if (memberInfo.isFollow_by_me()) {
            mDataBinding.follow.setBackgroundColor(getColor(R.color._C5C5C5));
            mDataBinding.follow.setText(getString(R.string.canel_follow));
        } else {
            mDataBinding.follow.setBackgroundColor(getColor(R.color._101010));
            mDataBinding.follow.setText(getString(R.string.text_follow));
        }
        mDataBinding.follow.setOnClickListener(v -> {
            if (memberInfo.isFollow_by_me()) {
                unFollow();
            } else {
                follow();
            }
        });
        if (TextUtils.isEmpty(memberInfo.getDesc())) {
            mDataBinding.artDesc.setText(R.string.set_desc_tips);
//            mDataBinding.artDesc.setOnClickListener(v -> startActivity(UserDescActivity.class));
        } else
            mDataBinding.artDesc.setText(memberInfo.getDesc());
    }

    public void getPopular(String uid) {
        RequestManager.instance().searchUserArts(uid, new MinerCallback<BaseResponseVo<List<SellingArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                {
                    if (response.body() != null)
                        popularList = response.body().getBody();
                    if (popularList != null && popularList.size() > 0) {
                        homeHotFragment.updateData(popularList);
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });

    }

    private void getAuctions(String uid) {
        RequestManager.instance().searchUserAuctionArts(uid, new MinerCallback<BaseResponseVo<List<AuctionArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<AuctionArtVo>>> call, Response<BaseResponseVo<List<AuctionArtVo>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) return;
                    if (response.body().getBody() != null)
                        auctionList = response.body().getBody();

                    if (auctionList != null && auctionList.size() > 0) {
                        homeAuctionFragment.updateData(auctionList);
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<AuctionArtVo>>> call, Response<BaseResponseVo<List<AuctionArtVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    public void getMemberInfo(String uid) {

        RequestManager.instance().searchMemberInfo(uid, new MinerCallback<BaseResponseVo<MemberInfo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<MemberInfo>> call, Response<BaseResponseVo<MemberInfo>> response) {
                if (response.isSuccessful()) {
                    memberInfo = response.body().getBody();
                    initPageData();
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<MemberInfo>> call, Response<BaseResponseVo<MemberInfo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    public void follow() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(uid));
        RequestManager.instance().followAction(uid, params, new MinerCallback<BaseResponseVo<MemberInfo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<MemberInfo>> call, Response<BaseResponseVo<MemberInfo>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getBody() != null) {
                        ToastUtils.showShort(getString(R.string.text_follow_success));
                        memberInfo = response.body().getBody();
                        initPageData();
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<MemberInfo>> call, Response<BaseResponseVo<MemberInfo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    public void unFollow() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(uid));
        RequestManager.instance().unFollow(uid, params, new MinerCallback<BaseResponseVo<MemberInfo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<MemberInfo>> call, Response<BaseResponseVo<MemberInfo>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getBody() != null) {
                        ToastUtils.showShort(getString(R.string.text_cancle_follow_success));
                        memberInfo = response.body().getBody();
                        initPageData();
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<MemberInfo>> call, Response<BaseResponseVo<MemberInfo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    @Override
    public Fragment getFragment(int position) {
        if (position == 0) {
            return homeHotFragment;
        }
        return homeAuctionFragment;
    }
}