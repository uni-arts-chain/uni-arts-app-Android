package com.yunhualian.ui.activity.user;


import android.text.TextUtils;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yunhualian.R;
import com.yunhualian.adapter.UserHomePagePicturesAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityUserHomePageBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.MemberInfo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.utils.StringUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class UserHomePageActivity extends BaseActivity<ActivityUserHomePageBinding> {

    List<SellingArtVo> artBeanList;
    UserHomePagePicturesAdapter adapter;
    int uid;
    MemberInfo memberInfo;

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

        uid = getIntent().getIntExtra("uid", 0);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mDataBinding.artList.setLayoutManager(layoutManager);
        adapter = new UserHomePagePicturesAdapter(artBeanList);
        mDataBinding.artList.setAdapter(adapter);
        getPopular(String.valueOf(uid));
        getMemberInfo(String.valueOf(uid));
    }

    public void initPageData() {
        if (null == memberInfo) return;

        mDataBinding.name.setText(memberInfo.getDisplay_name());
        RequestOptions requestOptions = new RequestOptions().placeholder(R.mipmap.icon_default_head);
        Glide.with(this).load(memberInfo.getAvatar().getUrl()).apply(requestOptions).into(mDataBinding.mineTitleImg);

        if (memberInfo.isFollow_by_me()) {
            mDataBinding.follow.setBackgroundColor(getColor(R.color._C5C5C5));
            mDataBinding.follow.setText("取消关注");
        } else {
            mDataBinding.follow.setBackgroundColor(getColor(R.color._101010));
            mDataBinding.follow.setText("关注");
        }
        mDataBinding.follow.setOnClickListener(v -> {
            if (memberInfo.isFollow_by_me()) {
                unFollow();
            } else {
                follow();
            }
        });
        if (TextUtils.isEmpty(memberInfo.getArtist_desc())) {
            mDataBinding.artDesc.setText(R.string.set_desc_tips);
            mDataBinding.artDesc.setOnClickListener(v -> startActivity(UserDescActivity.class));
        } else
            mDataBinding.artDesc.setText(memberInfo.getArtist_desc());
    }

    public void getPopular(String uid) {
        RequestManager.instance().searchUserArts(uid, new MinerCallback<BaseResponseVo<List<SellingArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                if (response.isSuccessful()) {
                    artBeanList = response.body().getBody();
//                    if (artBeanList.size() > 0)
                    adapter.setNewData(artBeanList);
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
                        ToastUtils.showShort("关注成功");
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
                        ToastUtils.showShort("取消关注成功");
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
}