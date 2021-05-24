package com.yunhualian.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;

import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityNoticeInfoBinding;
import com.yunhualian.entity.AnnouncementVo;

public class NoticeInfoActivity extends BaseActivity<ActivityNoticeInfoBinding> {
    AnnouncementVo announcementVo;
    public static final String NOTIC = "notic";

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_info;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        announcementVo = (AnnouncementVo) getIntent().getSerializableExtra(NOTIC);

        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        String msg = announcementVo == null ? "公告" : announcementVo.getTitle();
//        mToolBarOptions.titleId = R.string.message;
        mToolBarOptions.titleString = msg;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        if (announcementVo != null) {
            mDataBinding.content.setText(Html.fromHtml(announcementVo.getContent()));
        }
    }
}