package com.yunhualian.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.yunhualian.R;
import com.yunhualian.adapter.CreatorParentAdapter;
import com.yunhualian.adapter.CreatorTopAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.FragmentCreatorBinding;
import com.yunhualian.entity.ArtistListVo;
import com.yunhualian.entity.ArtistVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.user.MyHomePageActivity;
import com.yunhualian.ui.activity.user.UserHomePageActivity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CreatorFragment extends BaseFragment<FragmentCreatorBinding> {

    CreatorParentAdapter creatorParentAdapter;
    List<ArtistListVo> lists;
    List<ArtistVo> topList;
    int page = 1;
    int perpage = 20;
    CreatorTopAdapter creatorTopAdapter;

    public static BaseFragment newInstance() {
        CreatorFragment fragment = new CreatorFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_creator;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        creatorParentAdapter = new CreatorParentAdapter(lists);
        LinearLayoutManager sortLayoutManager = new LinearLayoutManager(YunApplication.getInstance());
        mBinding.artistList.setLayoutManager(sortLayoutManager);
        mBinding.artistList.setAdapter(creatorParentAdapter);
        creatorTopAdapter = new CreatorTopAdapter(topList);
        mBinding.topArtist.setLayoutManager(new LinearLayoutManager(YunApplication.getInstance()));
        mBinding.topArtist.setAdapter(creatorTopAdapter);
        creatorParentAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (lists.size() > 0) {
                goUserHomePage((lists.get(position).getMember().getId()));
            }

        });
        getTopArtist();
        getTopArtistList();
        mBinding.swipeRefresh.setOnRefreshListener(() -> {
            mBinding.swipeRefresh.setRefreshing(false);
            getTopArtist();
            getTopArtistList();
        });
        creatorTopAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (topList.get(position).getId() == YunApplication.getmUserVo().getId()) {
                startActivity(MyHomePageActivity.class);
            } else goUserHomePage(topList.get(position).getId());
        });
    }

//    private void initTopArtistInfo(ArtistVo artistVo) {
//        if (artistVo == null) return;
//        Glide.with(mActivity).load(artistVo.getRecommend_image().getUrl()).transition(withCrossFade()).into(mBinding.artistPic);
//        mBinding.artistName.setText(artistVo.getDisplay_name());
//
//        mBinding.topArtist.setOnClickListener(v -> {
//            if (artistVo.getId() == YunApplication.getmUserVo().getId()) {
//                startActivity(MyHomePageActivity.class);
//            } else
//                goUserHomePage(artistVo.getId());
//        });
//    }

    private void goUserHomePage(int uid) {
        Bundle bundle = new Bundle();
        bundle.putInt(UserHomePageActivity.UID, uid);
        startActivity(UserHomePageActivity.class, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void getTopArtist() {
        RequestManager.instance().getTopArtist(new MinerCallback<BaseResponseVo<List<ArtistVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<ArtistVo>>> call, Response<BaseResponseVo<List<ArtistVo>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getBody().size() > BigDecimal.ZERO.intValue()) {

                            topList = response.body().getBody();
                            creatorTopAdapter.setNewData(topList);
//                            initTopArtistInfo(response.body().getBody().get(BigDecimal.ZERO.intValue()));
                        }
                    }

                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<ArtistVo>>> call, Response<BaseResponseVo<List<ArtistVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    public void getTopArtistList() {
        showLoading(getString(R.string.progress_loading));
        HashMap<String, String> param = new HashMap<>();
        param.put("page", String.valueOf(page));
        param.put("per_page", String.valueOf(perpage));
        RequestManager.instance().getTopArtistList(param, new MinerCallback<BaseResponseVo<List<ArtistListVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<ArtistListVo>>> call, Response<BaseResponseVo<List<ArtistListVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        lists = response.body().getBody();
                        creatorParentAdapter.setNewData(lists);
                        if (lists.size() == BigDecimal.ZERO.intValue()) {
                            mBinding.layoutTitle.setVisibility(View.GONE);
                        } else mBinding.layoutTitle.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<ArtistListVo>>> call, Response<BaseResponseVo<List<ArtistListVo>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }
}