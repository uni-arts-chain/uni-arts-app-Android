package com.yunhualian.ui.activity.user;


import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yunhualian.R;
import com.yunhualian.adapter.FollowAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityFollowBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.FollowerVO;
import com.yunhualian.entity.MemberInfo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FollowAndFansActivity extends BaseActivity<ActivityFollowBinding> {

    FollowAdapter messagesAdapter;
    List<FollowerVO> followerVOList;
    String from;
    int clickPosition = 0;
    public static final String FOLLOW = "follor";
    public static final String FANS = "fans";

    @Override
    public int getLayoutId() {
        return R.layout.activity_follow;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        from = getIntent().getExtras().getString("from");

        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        LogUtils.e("path === " + getCacheDir().getAbsolutePath());
        mToolBarOptions.titleId = from.equals(FOLLOW) ? R.string.follw_title : R.string.fans_title;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        messagesAdapter = new FollowAdapter(followerVOList);
        mDataBinding.messageList.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter.setEmptyView(R.layout.layout_entrust_empty, mDataBinding.messageList);
        mDataBinding.messageList.setAdapter(messagesAdapter);
        messagesAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            clickPosition = position;
            if (followerVOList.get(position).isFollow_by_me()) {
                unFollow(position);
            } else {
                follow(position);
            }
        });
        messagesAdapter.setOnItemClickListener((adapter, view, position) -> {
            Bundle bundle1 = new Bundle();
            bundle1.putInt(UserHomePageActivity.UID, followerVOList.get(position).getId());
            startActivity(UserHomePageActivity.class, bundle1);
        });
        mDataBinding.swipeRefresh.setOnRefreshListener(() -> {
            if (from.equals(FOLLOW)) {
                queryFollowing();
            } else {
                queryFollowers();
            }
            mDataBinding.swipeRefresh.setRefreshing(false);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (from.equals(FOLLOW)) {
            queryFollowing();
        } else {
            queryFollowers();
        }
    }

    private void queryFollowing() {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("per_page", "10");
        RequestManager.instance().queryFollowings(params, new MinerCallback<BaseResponseVo<List<FollowerVO>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<FollowerVO>>> call, Response<BaseResponseVo<List<FollowerVO>>> response) {
                if (response.isSuccessful()) {
                    if (followerVOList != null && followerVOList.size() > 0) followerVOList.clear();
                    followerVOList = response.body().getBody();
                    if (followerVOList.size() > 0)
                        messagesAdapter.setNewData(followerVOList);
                    dismissLoading();
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<FollowerVO>>> call, Response<BaseResponseVo<List<FollowerVO>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });

    }

    private void queryFollowers() {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("per_page", "10");
        RequestManager.instance().queryFollowers(params, new MinerCallback<BaseResponseVo<List<FollowerVO>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<FollowerVO>>> call, Response<BaseResponseVo<List<FollowerVO>>> response) {
                if (response.isSuccessful()) {
                    if (followerVOList != null && followerVOList.size() > 0) followerVOList.clear();
                    followerVOList = response.body().getBody();
                    if (followerVOList.size() > 0)
                        messagesAdapter.setNewData(followerVOList);
                    dismissLoading();
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<FollowerVO>>> call, Response<BaseResponseVo<List<FollowerVO>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    public void follow(int position) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(followerVOList.get(position).getId()));
        RequestManager.instance().followAction(followerVOList.get(position).getId(), params, new MinerCallback<BaseResponseVo<MemberInfo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<MemberInfo>> call, Response<BaseResponseVo<MemberInfo>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getBody() != null) {
                        ToastUtils.showShort("关注成功");
                        followerVOList.get(position).setFollow_by_me(true);
                        messagesAdapter.setNewData(followerVOList);
//                        if (from.equals(FOLLOW)) {
//                            queryFollowing();
//                        } else {
//                            queryFollowers();
//                        }
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

    public void unFollow(int position) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(followerVOList.get(position).getId()));
        RequestManager.instance().unFollow(followerVOList.get(position).getId(), params, new MinerCallback<BaseResponseVo<MemberInfo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<MemberInfo>> call, Response<BaseResponseVo<MemberInfo>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getBody() != null) {
                        ToastUtils.showShort("取消关注成功");
                        followerVOList.get(position).setFollow_by_me(false);
                        messagesAdapter.setNewData(followerVOList);
//                        if (from.equals(FOLLOW)) {
//                            queryFollowing();
//                        } else {
//                            queryFollowers();
//                        }
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