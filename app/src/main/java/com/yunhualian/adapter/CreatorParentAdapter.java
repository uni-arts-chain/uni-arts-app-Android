package com.yunhualian.adapter;


import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;

import java.util.Arrays;
import java.util.List;

public class CreatorParentAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    CommonPictureAdapter homePageThemeAdapter;
    private List<String> sortList;

    public CreatorParentAdapter(List<String> data,Context context) {
        super(R.layout.fragment_creator_parent_item, data);
        sortList = Arrays.asList(context.getResources().getStringArray(R.array.popular));
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int position = helper.getPosition();
        homePageThemeAdapter = new CommonPictureAdapter(getData());
        LinearLayoutManager sortLayoutManager = new LinearLayoutManager(YunApplication.getInstance());
        sortLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        RecyclerView recyclerView = helper.getView(R.id.picture_list);
        recyclerView.setLayoutManager(sortLayoutManager);
        recyclerView.setAdapter(homePageThemeAdapter);
    }
}
