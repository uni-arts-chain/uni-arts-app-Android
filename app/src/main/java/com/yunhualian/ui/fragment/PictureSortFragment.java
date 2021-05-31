package com.yunhualian.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yunhualian.R;
import com.yunhualian.adapter.PicturesAdapter;
import com.yunhualian.adapter.PrizeAdapter;
import com.yunhualian.adapter.SortAdapter;
import com.yunhualian.adapter.TypeAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.FragmentPictureSortBinding;
import com.yunhualian.entity.ArtPriceVo;
import com.yunhualian.entity.ArtTypeVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.GetBaseData;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.art.ArtDetailActivity;
import com.yunhualian.ui.activity.SearchActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class PictureSortFragment extends BaseFragment<FragmentPictureSortBinding> implements BaseQuickAdapter.OnItemClickListener {
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
    StaggeredGridLayoutManager layoutManager;
    int perpage = 20;
    int page = 1;

    long lastRefreshTime = 0;
    int timeFlag = 3 * 1000;

    public static BaseFragment newInstance() {
        PictureSortFragment fragment = new PictureSortFragment();
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_picture_sort;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        artBeanList = new ArrayList<>();
        typeList = YunApplication.getArtTypelist() != null ?
                YunApplication.getArtTypelist() : queryTypeList();
        materialVos = YunApplication.getArtThemeVoList() != null ?
                YunApplication.getArtThemeVoList() : queryThmeList();
        priceVos = YunApplication.getArtPriceVoList() != null ?
                YunApplication.getArtPriceVoList() : queryPriceList();
        initRefresh();
        mBinding.searchEdt.setOnClickListener(v -> startActivity(SearchActivity.class));
        sortAdapter = new SortAdapter(materialVos);
        typeAdapter = new TypeAdapter(typeList);
        prizeAdapter = new PrizeAdapter(priceVos);
        picturesAdapter = new PicturesAdapter(artBeanList);
        initSelectedListener();
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);


        mBinding.scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            layoutManager.invalidateSpanAssignments();
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                //滑动到底部
                if (System.currentTimeMillis() - lastRefreshTime > timeFlag) {
                    lastRefreshTime = System.currentTimeMillis();
                    getPopular(param);
                }
            }
        });

        mBinding.pictures.setLayoutManager(layoutManager);
        picturesAdapter.setEmptyView(R.layout.layout_entrust_empty, mBinding.pictures);
        picturesAdapter.setOnLoadMoreListener(() -> {

        }, mBinding.pictures);
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
                page = 1;
                param.put("category_id", String.valueOf(materialVos.get(selectPosition).getId()));
                getPopular(param);
                sortAdapter.notifyDataSetChanged();
            }

            @Override
            public void onUnSelected(int selectPosition) {
                if (param.containsKey("category_id")) {
                    page = 1;
                    param.remove("category_id");
                    getPopular(param);
                    sortAdapter.notifyDataSetChanged();
                }
            }
        });
        typeAdapter.addSelectedListener(new TypeAdapter.onSelectedListener() {
            @Override
            public void onSelected(int selectPosition) {
                page = 1;
                param.put("resource_type", String.valueOf(typeList.get(selectPosition).getId()));
                getPopular(param);
                typeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onUnSelected(int selectPosition) {

                if (param.containsKey("resource_type")) {
                    page = 1;
                    param.remove("resource_type");
                    getPopular(param);
                    typeAdapter.notifyDataSetChanged();
                }
            }
        });
        prizeAdapter.addSelectedListener(new PrizeAdapter.onSelectedListener() {
            @Override
            public void onSelected(int selectPosition) {
                page = 1;
                param.put("price_sort", String.valueOf(priceVos.get(selectPosition).getId()));
                getPopular(param);
                prizeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onUnSelected(int selectPosition) {
                if (param.containsKey("price_sort")) {
                    page = 1;
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
        mBinding.srlShoopingMall.setOnRefreshListener(() -> {
            page = BigDecimal.ONE.intValue();
            param = param == null ? new HashMap<>() : param;
            getPopular(param);
            mBinding.srlShoopingMall.setRefreshing(false);
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
            bundle.putInt(ArtDetailActivity.ART_ID, artBeanList.get(position).getId());
            startActivity(ArtDetailActivity.class, bundle);
        }
    }

    public void getPopular(HashMap<String, String> params) {
        showLoading(getString(R.string.progress_loading));
        params.put("page", String.valueOf(page));
        params.put("per_page", String.valueOf(perpage));
        RequestManager.instance().querySelling(params, new MinerCallback<BaseResponseVo<List<SellingArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getBody() != null) {
                        if (response.body().getBody().size() == 0) return;
                        if (page == BigDecimal.ONE.intValue()) {
                            artBeanList.clear();
                            artBeanList = response.body().getBody();
                            picturesAdapter.setNewData(artBeanList);
                        } else if (page > BigDecimal.ONE.intValue() && artBeanList.size() > BigDecimal.ZERO.intValue()) {
                            artBeanList.addAll(response.body().getBody());
                            picturesAdapter.notifyItemRangeChanged(artBeanList.size() - 1, response.body().getBody().size());
                        }

//                        if (page > BigDecimal.ONE.intValue()) {
                        picturesAdapter.loadMoreEnd();
//                        }
                        page++;
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

    private List<ArtTypeVo> queryTypeList() {
        GetBaseData.getArtType(() -> {
            if (YunApplication.getArtTypelist() != null) {
                typeList = YunApplication.getArtTypelist();
                typeAdapter.setNewData(typeList);
            }
        });
        return new ArrayList<>();
    }

    private List<ArtTypeVo> queryThmeList() {
        GetBaseData.getCategories(() -> {
            materialVos = YunApplication.getArtThemeVoList();
            sortAdapter.setNewData(materialVos);
        });
        return new ArrayList<>();
    }

    private List<ArtPriceVo> queryPriceList() {
        GetBaseData.getPrice(() -> {
            priceVos = YunApplication.getArtPriceVoList();
            prizeAdapter.setNewData(priceVos);
        });
        return new ArrayList<>();
    }

}