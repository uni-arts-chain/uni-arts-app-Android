package com.gammaray.ui.activity;

import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.databinding.ActivityGuideBinding;
import com.gammaray.widget.GuideView;

import jp.co.soramitsu.app.root.presentation.RootActivity;


/**
 * @Description: 引导页
 * @Author: Houbc
 * @CreateDate: 2020/7/14 10:34
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @VersionLog: 1.0
 */

public class GuideActivity extends BaseActivity<ActivityGuideBinding> {
    private final int[] mPageImages = {
            R.mipmap.guide1,
            R.mipmap.guide2,
            R.mipmap.img_guide3,
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mDataBinding.guideView.setData(mPageImages, null, R.layout.guide_enter_view_layout, new GuideView.GuideCallBack() {
            @Override
            public void slidingPosition(int position) {

            }

            @Override
            public void slidingEnd() {

            }

            @Override
            public void onEndClickListener() {

            }

            @Override
            public void onEnterClickListener() {
                startActivity(RootActivity.class);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
