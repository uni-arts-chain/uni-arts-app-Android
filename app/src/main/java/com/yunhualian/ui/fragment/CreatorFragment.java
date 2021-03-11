package com.yunhualian.ui.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.yunhualian.R;
import com.yunhualian.adapter.CreatorParentAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.FragmentCreatorBinding;

import java.util.Arrays;
import java.util.List;

public class CreatorFragment extends BaseFragment<FragmentCreatorBinding> {

    CreatorParentAdapter creatorParentAdapter;
    List<String> lists;

    public static BaseFragment newInstance() {
        CreatorFragment fragment = new CreatorFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_creator_k;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        lists = Arrays.asList(getResources().getStringArray(R.array.popular));
        creatorParentAdapter = new CreatorParentAdapter(lists, YunApplication.getInstance());
        LinearLayoutManager sortLayoutManager = new LinearLayoutManager(YunApplication.getInstance());
        mBinding.artistList.setLayoutManager(sortLayoutManager);
        mBinding.artistList.setAdapter(creatorParentAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}