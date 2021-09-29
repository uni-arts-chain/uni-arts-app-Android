package com.gammaray.ui.activity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gammaray.R;
import com.gammaray.adapter.DAppFunctionAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.databinding.ActivityDappWebLayoutBinding;
import com.gammaray.entity.DappFunctionBean;
import com.gammaray.widget.BasePopupWindow;

import java.util.ArrayList;
import java.util.List;

public class DAppWebActivity extends BaseActivity<ActivityDappWebLayoutBinding> implements View.OnClickListener {

    private String mTitle;

    private PopupWindow mDAppFunctionsWindow;

    private List<DappFunctionBean> mFunctions = new ArrayList<>();

    private DappFunctionBean mCollect = new DappFunctionBean("收藏", R.mipmap.icon_a_collect);

    private DappFunctionBean mUnCollect = new DappFunctionBean("收藏", R.mipmap.icon_c_collect);

    private DappFunctionBean mCopyUrl = new DappFunctionBean("复制链接", R.mipmap.icon_url_copy);

    private DappFunctionBean mRefresh = new DappFunctionBean("刷新", R.mipmap.icon_refresh);

    private RecyclerView mFuncListView;

    private DAppFunctionAdapter mAdapter;

    private boolean bIsCollect;

    @Override
    public int getLayoutId() {
        return R.layout.activity_dapp_web_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        if (getIntent() != null) {
            mTitle = getIntent().getStringExtra("dapp_name");
            bIsCollect = getIntent().getBooleanExtra("collect", false);

        }
        mDataBinding.tvAppName.setText(mTitle);
        mDataBinding.rlFunctions.setOnClickListener(this);
        mDataBinding.rlClose.setOnClickListener(this);

        if (bIsCollect) {
            mFunctions.add(mCollect);
        } else {
            mFunctions.add(mUnCollect);
        }
        mFunctions.add(mCopyUrl);
        mFunctions.add(mRefresh);

        View functionView = LayoutInflater.from(this).inflate(R.layout.pop_dapp_manage_layout, null);
        mFuncListView = functionView.findViewById(R.id.rv_dapp_functions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mFuncListView.setLayoutManager(layoutManager);
        mAdapter = new DAppFunctionAdapter(mFunctions);
        mFuncListView.setAdapter(mAdapter);

        mDAppFunctionsWindow = new BasePopupWindow(this);
        mDAppFunctionsWindow.setContentView(functionView);
        mDAppFunctionsWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mDAppFunctionsWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mDAppFunctionsWindow.setOutsideTouchable(false);
        mDAppFunctionsWindow.setTouchable(true);
        mDAppFunctionsWindow.setAnimationStyle(R.style.mypopwindow_anim_style);

        functionView.findViewById(R.id.tv_hint_close).setOnClickListener(view -> {
            if (mDAppFunctionsWindow != null) {
                mDAppFunctionsWindow.dismiss();
            }
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position == 0) {
                if (bIsCollect) {
                    mFunctions.get(position).setDappIcon(R.mipmap.icon_c_collect);
                } else {
                    mFunctions.get(position).setDappIcon(R.mipmap.icon_a_collect);
                }
                mAdapter.notifyDataSetChanged();
            } else if (position == 1) {

            } else if (position == 2) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_functions) {
            mDAppFunctionsWindow.showAtLocation(mDataBinding.parentLayout, Gravity.BOTTOM, 0, 0);
        } else if (view.getId() == R.id.rl_close) {
            finish();
        }
    }
}
