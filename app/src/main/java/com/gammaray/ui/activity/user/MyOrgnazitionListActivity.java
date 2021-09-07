package com.gammaray.ui.activity.user;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.gammaray.R;
import com.gammaray.adapter.OrgnazationListAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityMyOrgnazitionListBinding;

import java.util.Arrays;
import java.util.List;

public class MyOrgnazitionListActivity extends BaseActivity<ActivityMyOrgnazitionListBinding> {

    OrgnazationListAdapter listAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_orgnazition_list;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.my_orgnaziton;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.my_page_tabs));
        listAdapter = new OrgnazationListAdapter(list, this);
        mDataBinding.organList.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.organList.setAdapter(listAdapter);
        listAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {

                case R.id.pass:
                    ToastUtils.showLong("通过" + position);
                    break;
                case R.id.refuse:
                    ToastUtils.showLong("拒绝" + position);
                    break;
            }
        });
    }
}