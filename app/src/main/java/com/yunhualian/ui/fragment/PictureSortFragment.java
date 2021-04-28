package com.yunhualian.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yunhualian.R;
import com.yunhualian.adapter.PicturesAdapter;
import com.yunhualian.adapter.PrizeAdapter;
import com.yunhualian.adapter.SortAdapter;
import com.yunhualian.adapter.TypeAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.FragmentPictureSortBinding;
import com.yunhualian.entity.ArtMaterialVo;
import com.yunhualian.entity.ArtPriceVo;
import com.yunhualian.entity.ArtTypeVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.ArtDetailActivity;
import com.yunhualian.ui.activity.SearchActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class PictureSortFragment extends BaseFragment<FragmentPictureSortBinding> implements BaseQuickAdapter.OnItemClickListener {
    private List<String> prizeList, pictureList;
    private SortAdapter sortAdapter;
    private TypeAdapter typeAdapter;
    private PrizeAdapter prizeAdapter;
    private PicturesAdapter picturesAdapter;

    private List<SellingArtVo> artBeanList;
    private List<ArtTypeVo> typeList;
    private List<ArtTypeVo> materialVos;
    private List<ArtPriceVo> priceVos;
    HashMap<String, String> param = new HashMap<>();
    EditText searchText;
    private int refreshIndex = 0;

    public static BaseFragment newInstance() {
        PictureSortFragment fragment = new PictureSortFragment();
        return fragment;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (refreshIndex > 0) return;
            getPopular(new HashMap<>());
            refreshIndex++;
        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_picture_sort;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        typeList = YunApplication.getArtTypelist() != null ?
                YunApplication.getArtTypelist() : new ArrayList<>();
        materialVos = YunApplication.getArtThemeVoList() != null ?
                YunApplication.getArtThemeVoList() : new ArrayList<>();
        priceVos = YunApplication.getArtPriceVoList() != null ?
                YunApplication.getArtPriceVoList() : new ArrayList<>();
        initRefresh();
        mBinding.searchEdt.setOnClickListener(v -> startActivity(SearchActivity.class));
        prizeList = new ArrayList<>();
        prizeList = Arrays.asList(getResources().getStringArray(R.array.sorts));
        pictureList = Arrays.asList(getResources().getStringArray(R.array.sorts));
        sortAdapter = new SortAdapter(materialVos);
        typeAdapter = new TypeAdapter(typeList);
        prizeAdapter = new PrizeAdapter(priceVos);
        picturesAdapter = new PicturesAdapter(artBeanList);

        initSelectedListener();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //防止Item切换
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mBinding.pictures.setLayoutManager(layoutManager);
        mBinding.pictures.setAdapter(picturesAdapter);
        picturesAdapter.setOnItemClickListener(this);
        LinearLayoutManager sortLayoutManager = new LinearLayoutManager(YunApplication.getInstance());
        sortLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        LinearLayoutManager typeLayoutManager = new LinearLayoutManager(YunApplication.getInstance());
        typeLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        LinearLayoutManager prizeLayoutManager = new LinearLayoutManager(YunApplication.getInstance());
        prizeLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mBinding.sortList.setLayoutManager(sortLayoutManager);
        mBinding.typeList.setLayoutManager(typeLayoutManager);
        mBinding.prizeList.setLayoutManager(prizeLayoutManager);
        mBinding.sortList.setAdapter(sortAdapter);
        mBinding.typeList.setAdapter(typeAdapter);
        mBinding.prizeList.setAdapter(prizeAdapter);
        getPopular(new HashMap<>());
    }

    public void initSelectedListener() {
        sortAdapter.addSelectedListener(new SortAdapter.onSelectedListener() {
            @Override
            public void onSelected(int selectPosition) {
                param.put("category_id", String.valueOf(typeList.get(selectPosition).getId()));
                getPopular(param);
                sortAdapter.notifyDataSetChanged();
            }

            @Override
            public void onUnSelected(int selectPosition) {
                if (param.containsKey("category_id")) {
                    param.remove("category_id");
                    getPopular(param);
                    sortAdapter.notifyDataSetChanged();
                }
            }
        });
        typeAdapter.addSelectedListener(new TypeAdapter.onSelectedListener() {
            @Override
            public void onSelected(int selectPosition) {
                param.put("theme_id", String.valueOf(materialVos.get(selectPosition).getId()));
                getPopular(param);
                typeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onUnSelected(int selectPosition) {
                if (param.containsKey("theme_id")) {
                    param.remove("theme_id");
                    getPopular(param);
                    typeAdapter.notifyDataSetChanged();
                }
            }
        });
        prizeAdapter.addSelectedListener(new PrizeAdapter.onSelectedListener() {
            @Override
            public void onSelected(int selectPosition) {
                param.put("price_sort", String.valueOf(priceVos.get(selectPosition).getId()));
                getPopular(param);
                prizeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onUnSelected(int selectPosition) {
                if (param.containsKey("price_sort")) {
                    param.remove("price_sort");
                    getPopular(param);
                    prizeAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initRefresh() {
        mBinding.srlShoopingMall.setColorSchemeResources(R.color.colorAccent);
        mBinding.srlShoopingMall.setDistanceToTriggerSync(500);
        mBinding.srlShoopingMall.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                param = param == null ? new HashMap<>() : param;
                getPopular(param);
                mBinding.srlShoopingMall.setRefreshing(false);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
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
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (artBeanList != null && artBeanList.size() > position) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ArtDetailActivity.ART_KEY, artBeanList.get(position));
            startActivity(ArtDetailActivity.class, bundle);
        }
    }

    public void getPopular(HashMap<String, String> params) {
        showLoading("加载中...");
        params.put("page", "1");
        params.put("per_page", "100");
        RequestManager.instance().querySelling(params, new MinerCallback<BaseResponseVo<List<SellingArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                if (response.isSuccessful()) {
                    artBeanList = response.body().getBody();
//                    if (artBeanList.size() > 0)
                    picturesAdapter.setNewData(artBeanList);
                    handler.sendEmptyMessageDelayed(0, 500);
                    dismissLoading();
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
}