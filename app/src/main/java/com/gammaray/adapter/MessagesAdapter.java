package com.gammaray.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.constant.ExtraConstant;
import com.gammaray.entity.MessagesVo;
import com.gammaray.utils.DateUtil;

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
