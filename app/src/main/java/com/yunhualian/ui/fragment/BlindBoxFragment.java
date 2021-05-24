package com.yunhualian.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.yunhualian.R;
import com.yunhualian.adapter.BlindBoxAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.FragmentShoppingCartBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.BlindBoxVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.blindbox.BlindBoxDetailActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class BlindBoxFragment extends BaseFragment<FragmentShoppingCartBinding> {

    private View mEmptyView;
    BlindBoxAdapter adapter;

    private List<BlindBoxVo> popularList;
    int page = 1;

    public static BaseFragment newInstance() {
        BlindBoxFragment fragment = new BlindBoxFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_shopping_cart;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        mBinding.txtTitle.setText(R.string.tab_assets);
        adapter = new BlindBoxAdapter(popularList);
        mBinding.blindBoxList.setLayoutManager(new LinearLayoutManager(getContext()));

        mBinding.blindBoxList.setAdapter(adapter);

        mBinding.swipeRefresh.setOnRefreshListener(() -> {
            queryBindBox();
            mBinding.swipeRefresh.setRefreshing(false);
        });
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("box", popularList.get(position));
            startActivity(BlindBoxDetailActivity.class, bundle);

        });
        queryBindBox();
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

    private void queryBindBox() {
        showLoading(getString(R.string.progress_loading));
        RequestManager.instance().queryBlindBoxes(new MinerCallback<BaseResponseVo<List<BlindBoxVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<BlindBoxVo>>> call, Response<BaseResponseVo<List<BlindBoxVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    popularList = response.body().getBody();

                    if (popularList.size() > 0) {
//                        new getDesc().execute(popularList);
                        adapter.setNewData(popularList);
                    } else adapter.setEmptyView(getEmptyView());
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<BlindBoxVo>>> call, Response<BaseResponseVo<List<BlindBoxVo>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });

    }

    private View getEmptyView() {
        if (null == mEmptyView)
            mEmptyView = LayoutInflater.from(mActivity).inflate(R.layout.layout_entrust_empty, null);
        return mEmptyView;
    }
}