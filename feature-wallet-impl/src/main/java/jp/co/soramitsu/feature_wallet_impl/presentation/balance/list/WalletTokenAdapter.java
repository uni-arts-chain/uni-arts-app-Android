package jp.co.soramitsu.feature_wallet_impl.presentation.balance.list;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import jp.co.soramitsu.feature_wallet_impl.R;

public class WalletTokenAdapter extends BaseQuickAdapter<WalletTokenBean, BaseViewHolder> {

    List<WalletTokenBean> list;

    public WalletTokenAdapter(List<WalletTokenBean> list) {
        super(R.layout.item_wallet_token_layout, list);
        this.list = list;
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletTokenBean item) {
        helper.setImageResource(R.id.img_links_icon, item.getImgId());
        helper.setText(R.id.tv_links_short_name, item.getShortName());
        helper.setText(R.id.tv_links_name, item.getName());
        helper.setText(R.id.tv_remain,item.getBalance());
    }
}
