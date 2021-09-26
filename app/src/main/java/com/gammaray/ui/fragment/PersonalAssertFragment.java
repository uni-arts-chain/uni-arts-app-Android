package com.gammaray.ui.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gammaray.R;
import com.gammaray.adapter.WalletTokenAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.databinding.FragmentPersonalAssertLayoutBinding;
import com.gammaray.entity.WalletTokenBean;

import java.util.ArrayList;
import java.util.List;


public class PersonalAssertFragment extends BaseFragment<FragmentPersonalAssertLayoutBinding> {

    private String mType;

    private WalletTokenAdapter mAdapter;

    private List<WalletTokenBean> mWalletTokens;

    public PersonalAssertFragment(){

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

        mWalletTokens = new ArrayList<>();
//        mWalletTokens.add(new WalletTokenBean("111","1111",R.mipmap.icon_eth,"1111"));
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.rvAsserts.setLayoutManager(layoutManager);
        mAdapter = new WalletTokenAdapter(mWalletTokens);
        mBinding.rvAsserts.setAdapter(mAdapter);

    }

    public void updateData(List<WalletTokenBean> walletTokens){
        mWalletTokens = walletTokens;
        mAdapter.setNewData(mWalletTokens);
    }
}
