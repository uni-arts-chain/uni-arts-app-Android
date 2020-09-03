package com.yunhualian.ui.fragment;


import android.view.View;
import android.widget.Button;

import com.yunhualian.R;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.databinding.FragmentMineBinding;
import com.yunhualian.ui.activity.MyHomePageActivity;
import com.yunhualian.ui.activity.UpdatePictureActivity;

public class MineFragment extends BaseFragment<FragmentMineBinding> implements View.OnClickListener {

    Button button;

    public static BaseFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        mBinding.myPage.setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_page:
                startActivity(MyHomePageActivity.class);
                break;
        }
    }
}