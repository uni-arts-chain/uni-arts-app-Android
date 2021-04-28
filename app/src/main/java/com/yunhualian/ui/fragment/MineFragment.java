package com.yunhualian.ui.fragment;


import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yunhualian.R;
import com.yunhualian.adapter.MineActionAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.FragmentMineBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.CustomerServiceActivity;
import com.yunhualian.ui.activity.UploadArtActivity;
import com.yunhualian.ui.activity.user.FollowAndFansActivity;
import com.yunhualian.ui.activity.MessagesActivity;
import com.yunhualian.ui.activity.user.MyCollectActivity;
import com.yunhualian.ui.activity.user.MyHomePageActivity;
import com.yunhualian.ui.activity.order.SellAndBuyActivity;
import com.yunhualian.ui.activity.user.SellArtActivity;
import com.yunhualian.ui.activity.user.SettingsActivity;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MineFragment extends BaseFragment<FragmentMineBinding> implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    Button button;
    RecyclerView actionList;
    MineActionAdapter mineActionAdapter;
    public static final int MINE_PAGE = 0;
    public static final int UPGRADE_ARTS = 1;
    public static final int BUY_IN = 2;
    public static final int SELL_OUT = 3;
    public static final int COLLECT_ARTS = 4;
    public static final int ABOUT_US = 5;
    public static final int NEWS = 6;
    public static final int SERVICE = 7;

    public static BaseFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        initList();
//        initNick();
        mBinding.setting.setOnClickListener(this);
        mBinding.follow.setOnClickListener(this);
        mBinding.fans.setOnClickListener(this);

    }

    public void initList() {
        List<String> namelist = Arrays.asList(getResources().getStringArray(R.array.mine_actions));
        mineActionAdapter = new MineActionAdapter(namelist);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        mBinding.myActionList.setLayoutManager(layoutManager);
        mBinding.myActionList.setAdapter(mineActionAdapter);
        mineActionAdapter.setOnItemClickListener(this);
    }

    public void initNick() {

        String abc = "hello";
        String bcd = "aaaaaafsfsfsdfsworld";

        SpannableString spannableString = new SpannableString(bcd);
        SpannableString spannableString2 = new SpannableString(abc);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color._00D121));
        spannableString.setSpan(foregroundColorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mBinding.nickName.setText("" + bcd.indexOf("wo"));
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("MinFragment onResume...");
        getUserInfo();
    }

    private void initPageData() {
        UserVo userVo = YunApplication.getmUserVo();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.icon_default_head);
        Glide.with(getContext())
                .load(userVo.getAvatar().getUrl())
                .apply(requestOptions).into(mBinding.mineTitleImg);
        mBinding.follow.setText(
                getString(R.string.follow_num,
                        String.valueOf(userVo.getFollowing_user_size())));
        mBinding.fans.setText(getString(R.string.fans_num,
                String.valueOf(userVo.getFollow_user_size())));
        if (TextUtils.isEmpty(userVo.getDisplay_name()))
            mBinding.nickName.setText(R.string.set_display_name_tips);
        else
            mBinding.nickName.setText(userVo.getDisplay_name());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting:
                startActivity(SettingsActivity.class);
                break;
            case R.id.follow:
                Bundle bundle = new Bundle();
                bundle.putString("from", FollowAndFansActivity.FOLLOW);
                startActivity(FollowAndFansActivity.class, bundle);
                break;
            case R.id.fans:
                Bundle bundle2 = new Bundle();
                bundle2.putString("from", FollowAndFansActivity.FANS);
                startActivity(FollowAndFansActivity.class, bundle2);
                break;

        }
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (position) {
            case BUY_IN:
                Bundle buy = new Bundle();
                buy.putString("from", SellAndBuyActivity.BUY);
                startActivity(SellAndBuyActivity.class, buy);
                break;
            case SELL_OUT:
                Bundle sell = new Bundle();
                sell.putString("from", SellAndBuyActivity.SELL);
                startActivity(SellAndBuyActivity.class, sell);
                break;
            case NEWS:
                startActivity(MessagesActivity.class);
                break;
            case SERVICE:
                startActivity(CustomerServiceActivity.class);
                break;
            case MINE_PAGE:
                startActivity(MyHomePageActivity.class);
                break;
            case COLLECT_ARTS:
                startActivity(MyCollectActivity.class);
                break;
            case UPGRADE_ARTS:
                startActivity(UploadArtActivity.class);
                break;
//            case ADDRESS:
//                if (YunApplication.isLaunch) {
//                    startActivity(MyOrgnazitionListActivity.class);
//                } else
//                    startActivity(MyOrgnazitionAddActivity.class);
//                break;
//            case LAUNCH_AUCTION:
//                break;
//            case ADVICE:
//                startActivity(ShowLiveActivity.class);
//                break;
            case ABOUT_US:
                startActivity(SellArtActivity.class);
                break;
        }
    }

    private void getUserInfo() {
        RequestManager.instance().queryUser(new MinerCallback<BaseResponseVo<UserVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                if (response.isSuccessful()) {
                    YunApplication.setmUserVo(response.body().getBody());
                    initPageData();
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }
}