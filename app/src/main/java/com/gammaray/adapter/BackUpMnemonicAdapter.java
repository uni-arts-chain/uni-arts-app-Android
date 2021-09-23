package com.gammaray.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;

import java.util.List;

public class BackUpMnemonicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

//    private List<String> mnemonic;

    public BackUpMnemonicAdapter(List<String> data) {
        super(R.layout.item_backup_mnemonic_layout, data);
//        this.mnemonic = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_mnemonic_index, String.valueOf(helper.getAdapterPosition() + 1));
        helper.setText(R.id.tv_mnemonic_word, item);
    }
}
