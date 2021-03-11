package jp.co.soramitsu.app.root.presentation.tab;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.soramitsu.inport.R;
import jp.co.soramitsu.inport.databinding.FragmentPictureSortKBinding;

public class PictureSortFragment extends BaseFragment<FragmentPictureSortKBinding> {
    private List<String> sortList, typeList, prizeList;
    private SortAdapter sortAdapter, typeAdapter, prizeAdapter;
    private PicturesAdapter picturesAdapter;

    public static BaseFragment newInstance() {
        PictureSortFragment fragment = new PictureSortFragment();
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_picture_sort_k;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        initRefresh();
        mBinding.titleLayout.findViewById(R.id.layout_back).setVisibility(View.GONE);
        mBinding.titleLayout.findViewById(R.id.layout_menu).setVisibility(View.GONE);
        sortList = new ArrayList<>();
        typeList = new ArrayList<>();
        prizeList = new ArrayList<>();
        sortList = Arrays.asList(getResources().getStringArray(R.array.sorts));
        sortAdapter = new SortAdapter(sortList);
        initSelectedListener();

        typeAdapter = new SortAdapter(sortList);
        prizeAdapter = new SortAdapter(sortList);
        picturesAdapter = new PicturesAdapter(sortList, getContext());
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //防止Item切换
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mBinding.pictures.setLayoutManager(layoutManager);
        mBinding.pictures.setAdapter(picturesAdapter);
        LinearLayoutManager sortLayoutManager = new LinearLayoutManager(getContext());
        sortLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        LinearLayoutManager typeLayoutManager = new LinearLayoutManager(getContext());
        typeLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        LinearLayoutManager prizeLayoutManager = new LinearLayoutManager(getContext());
        prizeLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mBinding.sortList.setLayoutManager(sortLayoutManager);
        mBinding.typeList.setLayoutManager(typeLayoutManager);
        mBinding.prizeList.setLayoutManager(prizeLayoutManager);
        mBinding.sortList.setAdapter(sortAdapter);
        mBinding.typeList.setAdapter(typeAdapter);
        mBinding.prizeList.setAdapter(prizeAdapter);
    }

    public void initSelectedListener() {
        sortAdapter.addSelectedListener(new SortAdapter.onSelectedListener() {
            @Override
            public void onSelected(int selectPosition) {
//                ToastUtils.showShort("select" + sortList.get(selectPosition) + "||position == " + selectPosition);
            }

            @Override
            public void onUnSelected(int selectPosition) {

            }
        });
    }

    private void initRefresh() {
        mBinding.srlShoopingMall.setColorSchemeResources(R.color.colorAccent);
        mBinding.srlShoopingMall.setDistanceToTriggerSync(500);
        mBinding.srlShoopingMall.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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


}