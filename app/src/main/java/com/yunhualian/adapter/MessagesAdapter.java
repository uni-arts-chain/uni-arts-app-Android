package com.yunhualian.adapter;


import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.upbest.arouter.Extras;
import com.yunhualian.R;
import com.yunhualian.constant.ExtraConstant;
import com.yunhualian.entity.MessagesVo;
import com.yunhualian.utils.DateUtil;

import java.util.List;

public class MessagesAdapter extends BaseQuickAdapter<MessagesVo, BaseViewHolder> {

    public MessagesAdapter(List<MessagesVo> data) {
        super(R.layout.activity_messages_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessagesVo item) {
        helper.setText(R.id.title, item.getTitle());
        helper.setText(R.id.time, DateUtil.dateToStringWith(item.getCreated_at() * ExtraConstant.DEFAULT_TIME_EPLI));
        if (item.isRead()) {
            helper.setVisible(R.id.noRead, false);
        } else {
            helper.setVisible(R.id.noRead, true);
        }
    }
}
