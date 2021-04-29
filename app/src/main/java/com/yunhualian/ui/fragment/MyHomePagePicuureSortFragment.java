package com.yunhualian.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yunhualian.R;
import com.yunhualian.adapter.MyHomePageArtAdapter;
import com.yunhualian.adapter.PicturesAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.databinding.FragmentMyPagePictureSortBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.user.SellArtActivity;
import com.yunhualian.utils.MyStaggeredGridLayoutManager;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MyHomePagePicuureSortFragment extends BaseFragment<FragmentMyPagePictureSortBinding> {
    //    MyPicturesAdapter picturesAdapter;
    public static String STATE_ONLINE = "online";
    public static String STATE_AUCTION = "bidding";
    public static String STATE = "state";
    MyHomePageArtAdapter picturesAdapter;
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
        MyHomePagePicuureSortFragment fragment = new MyHomePagePicuureSortFragment();
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
        picturesAdapter = new MyHomePageArtAdapter(artVoList);
        StaggeredGridLayoutManager sortLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mBinding.pictures.setLayoutManager(sortLayoutManager);
        picturesAdapter.setEmptyView(R.layout.layout_entrust_empty, mBinding.pictures);
        mBinding.pictures.setAdapter(picturesAdapter);
        picturesAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            //去出售
            SellingArtVo sellingArtVo = null;
            if (artVoList.size() > 0) {
                sellingArtVo = artVoList.get(position);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(SellArtActivity.ARTINFO, sellingArtVo);
            startActivity(SellArtActivity.class, bundle);
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
                    if (artVoList != null && artVoList.size() > 0)
                        artVoList.clear();
                    artVoList = response.body().getBody();
                    if (artVoList.size() > 0) {
                        picturesAdapter.setNewData(artVoList);
                        dismissLoading();
                        if (!hasRefresh)
                            handler.sendEmptyMessageDelayed(0, 300);
                    }
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