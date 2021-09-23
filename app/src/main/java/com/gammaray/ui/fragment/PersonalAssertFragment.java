package com.gammaray.ui.fragment;

import com.gammaray.R;
import com.gammaray.base.BaseFragment;
import com.gammaray.databinding.FragmentPersonalAssertLayoutBinding;

public class PersonalAssertFragment extends BaseFragment<FragmentPersonalAssertLayoutBinding> {

    private String mType;

    public static BaseFragment newInstance(String type) {
        return new PersonalAssertFragment(type);
    }


    public PersonalAssertFragment(String type) {
        this.mType = type;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_personal_assert_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

    }
}
