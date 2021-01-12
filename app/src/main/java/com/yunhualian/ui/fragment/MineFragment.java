package com.yunhualian.ui.fragment;


import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sensetime.liveness.motion.ui.camera.SenseCamera;
import com.yunhualian.R;
import com.yunhualian.adapter.MineActionAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.FragmentMineBinding;
import com.yunhualian.ui.activity.CustomerServiceActivity;
import com.yunhualian.ui.activity.MessagesActivity;
import com.yunhualian.ui.activity.MyHomePageActivity;
import com.yunhualian.ui.activity.SettingsActivity;
import com.yunhualian.ui.activity.UpdatePictureActivity;
import com.yunhualian.ui.activity.VerifiedActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MineFragment extends BaseFragment<FragmentMineBinding> implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    Button button;
    RecyclerView actionList;
    MineActionAdapter mineActionAdapter;
    public static final int BUY_IN = 0;
    public static final int SELL_OUT = 1;
    public static final int NEWS = 2;
    public static final int SERVICE = 3;
    public static final int MINE_PAGE = 4;
    public static final int UPGRADE_ARTS = 5;
    public static final int ADDRESS = 6;
    public static final int LAUNCH_AUCTION = 7;
    public static final int ADVICE = 8;
    public static final int ABOUT_US = 9;

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
        initList();
//        initNick();
        mBinding.setting.setOnClickListener(this);
    }

    public void initList() {
        List<String> namelist = Arrays.asList(getResources().getStringArray(R.array.mine_actions));
        mineActionAdapter = new MineActionAdapter(namelist);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        mBinding.myActionList.setLayoutManager(layoutManager);
        mBinding.myActionList.setAdapter(mineActionAdapter);
        mineActionAdapter.setOnItemClickListener(this);
    }

    public void initNick() {

        String abc = "hello";
        String bcd = "aaaaaafsfsfsdfsworld";

        SpannableString spannableString = new SpannableString(bcd);
        SpannableString spannableString2 = new SpannableString(abc);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color._00D121));
        spannableString.setSpan(foregroundColorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mBinding.nickName.setText("" + bcd.indexOf("wo"));
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
            case R.id.setting:
                startActivity(SettingsActivity.class);
                break;
        }
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (position) {
            case BUY_IN:
                startActivity(UpdatePictureActivity.class);
                break;
            case SELL_OUT:

                break;
            case NEWS:
                startActivity(MessagesActivity.class);
                break;
            case SERVICE:
                startActivity(CustomerServiceActivity.class);
                break;
            case MINE_PAGE:
                startActivity(MyHomePageActivity.class);
                break;
            case UPGRADE_ARTS:

                break;
            case ADDRESS:

                break;
            case LAUNCH_AUCTION:

                break;
            case ADVICE:

                break;
            case ABOUT_US:

                break;
        }
    }

}