package com.yunhualian.ui.activity;

import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.yunhualian.MainActivity;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.constant.ExtraConstant;
import com.yunhualian.databinding.ActivityGuideBinding;
import com.yunhualian.widget.GuideView;

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
                CacheDiskStaticUtils.put(ExtraConstant.KEY_GUIDE_FLAG, "1");
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
