package com.yunhualian.ui.activity;


import androidx.recyclerview.widget.LinearLayoutManager;

import com.yunhualian.R;
import com.yunhualian.adapter.MessagesAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityMessagesBinding;

import java.util.Arrays;
import java.util.List;

public class MessagesActivity extends BaseActivity<ActivityMessagesBinding> {

    MessagesAdapter messagesAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_messages;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.physical_authentication;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.country_codes));
        messagesAdapter = new MessagesAdapter(list, this);
        mDataBinding.messageList.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.messageList.setAdapter(messagesAdapter);
    }
}