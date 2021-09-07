package com.gammaray.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.entity.HistoriesBean;
import com.gammaray.utils.DateUtil;

import java.util.List;

/**
 * @Date:2021/8/5
 * @Author:Created by peter_ben
 */
public class AccountHistoryAdapter extends BaseQuickAdapter<HistoriesBean, BaseViewHolder> {

    private static final int TIME = 1000;

    private String symbol;

    private String money;

    public AccountHistoryAdapter(List<HistoriesBean> data) {
        super(R.layout.item_bill_detail_layout, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, HistoriesBean item) {
        helper.setText(R.id.tv_bill_reason, item.getMessage());
        helper.setText(R.id.tv_bill_date, DateUtil.dateToStringWith(item.getCreated_at() * TIME));
        if (item.getAmount().contains("+") || item.getAmount().contains("-")) {
            symbol = item.getAmount().substring(0, 1);
            money = item.getAmount().substring(1);
            helper.setText(R.id.tv_bill_money, symbol + "￥" + money);
        } else {
            helper.setText(R.id.tv_bill_money, item.getAmount());
        }
    }
}
