package com.yunhualian.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.yunhualian.R;
import com.yunhualian.adapter.MyHomePageArtSellingAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.databinding.FragmentMyPagePictureSortBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.TransferActivity;
import com.yunhualian.ui.activity.art.ArtDetailActivity;
import com.yunhualian.ui.activity.user.SellArtActivity;
import com.yunhualian.ui.activity.user.SellArtUnCutActivity;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MyHomePagePicuureSortSellingFragment extends BaseFragment<FragmentMyPagePictureSortBinding> {
    //    MyPicturesAdapter picturesAdapter;
    public static String STATE_ONLINE = "online";
    public static String STATE_AUCTION = "bidding";
    public static String STATE = "state";
    public static int CUT_MODE = 3;
    MyHomePageArtSellingAdapter picturesAdapter;
    List<SellingArtVo> artVoList;
    private String state;
    private boolean hasRefresh = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            queryArts();
            hasRefresh = true;
        }
    };

    public static BaseFragment newInstance(String state) {
        MyHomePagePicuureSortSellingFragment fragment = new MyHomePagePicuureSortSellingFragment();
        Bundle args = new Bundle();
        args.putString(STATE, state);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_my_page_picture_sort;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        state = getArguments().getString(STATE);
        picturesAdapter = new MyHomePageArtSellingAdapter(artVoList);
        StaggeredGridLayoutManager sortLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mBinding.pictures.setLayoutManager(sortLayoutManager);
        picturesAdapter.setEmptyView(R.layout.layout_entrust_empty_for_homepage, mBinding.pictures);
        mBinding.pictures.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                sortLayoutManager.invalidateSpanAssignments();
            }
        });
        mBinding.pictures.setAdapter(picturesAdapter);
        picturesAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            //去下架
            SellingArtVo sellingArtVo = null;
            if (artVoList.size() > 0) {
                sellingArtVo = artVoList.get(position);
            }

            if (view.getId() == R.id.sellAction) {
                Bundle bundle = new Bundle();
                bundle.putInt(ArtDetailActivity.ART_ID, sellingArtVo.getId());
                bundle.putSerializable(SellArtActivity.ARTINFO, sellingArtVo);
                startActivity(ArtDetailActivity.class, bundle);
            } else if (view.getId() == R.id.transferAction) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(SellArtActivity.ARTINFO, sellingArtVo);
                startActivity(TransferActivity.class, bundle);
            }

        });
        mBinding.swipeRefresh.setOnRefreshListener(this::queryArts);
        picturesAdapter.setOnItemClickListener((adapter, view, position) -> {


            Bundle bundle = new Bundle();
            bundle.putSerializable(ArtDetailActivity.ART_KEY, artVoList.get(position));
            bundle.putInt(ArtDetailActivity.ART_ID, artVoList.get(position).getId());
            startActivity(ArtDetailActivity.class, bundle);

        });
    }


    @Override
    public void onResume() {
        super.onResume();
        queryArts();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void queryArts() {
        if (mBinding.swipeRefresh.isRefreshing()) {
            mBinding.swipeRefresh.setRefreshing(false);
        }
        showLoading("");
        HashMap<String, String> param = new HashMap<>();
        param.put("aasm_state", state);
        param.put("page", "1");
        param.put("per_page", "100");
        RequestManager.instance().queryMine(param, new MinerCallback<BaseResponseVo<List<SellingArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    artVoList = response.body().getBody();
                    picturesAdapter.setNewData(artVoList);
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }
}