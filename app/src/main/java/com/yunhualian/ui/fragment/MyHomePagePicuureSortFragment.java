package com.yunhualian.ui.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.yunhualian.R;
import com.yunhualian.adapter.MyHomePageAdapter;
import com.yunhualian.adapter.MyPicturesAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.FragmentMyPagePictureSortBinding;
import com.yunhualian.databinding.FragmentShoppingCartBinding;

import java.util.Arrays;
import java.util.List;

public class MyHomePagePicuureSortFragment extends BaseFragment<FragmentMyPagePictureSortBinding> {
    MyPicturesAdapter picturesAdapter;

    public static BaseFragment newInstance() {
        MyHomePagePicuureSortFragment fragment = new MyHomePagePicuureSortFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
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
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.sorts));
        picturesAdapter = new MyPicturesAdapter(list);
        LinearLayoutManager sortLayoutManager = new LinearLayoutManager(YunApplication.getInstance());
        mBinding.pictures.setLayoutManager(sortLayoutManager);
        mBinding.pictures.setAdapter(picturesAdapter);
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