package com.yunhualian.adapter;


import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;

import java.util.Arrays;
import java.util.List;

public class MineActionAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private List<String> nameList;
    private List<Integer> imgList;

    public MineActionAdapter(List<String> list) {
        super(R.layout.mine_action_item, list);
        nameList = list;
        imgList = Arrays.asList(R.mipmap.icon_mairu, R.mipmap.icon_maichu, R.mipmap.icon_xiaoxi,
                R.mipmap.icon_kefu, R.mipmap.icon_wodezhuye, R.mipmap.icon_shangchuanzuopin,
                R.mipmap.icon_shouhuodizhi, R.mipmap.icon_faqipaimai, R.mipmap.icon_yijianfankui,
                R.mipmap.icon_guanyuwomen);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setImageResource(R.id.action_icon, imgList.get(helper.getAdapterPosition()));
        helper.setText(R.id.action_name, item);
    }
}
