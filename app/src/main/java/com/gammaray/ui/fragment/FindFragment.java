package com.gammaray.ui.fragment;

import com.gammaray.R;
import com.gammaray.base.BaseFragment;
import com.gammaray.databinding.FragmentFindLayoutBinding;

public class FindFragment extends BaseFragment<FragmentFindLayoutBinding> {

    public static BaseFragment newInstance() {
        return new FindFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_find_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

    }
}
