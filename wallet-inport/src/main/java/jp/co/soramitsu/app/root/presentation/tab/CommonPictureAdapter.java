package jp.co.soramitsu.app.root.presentation.tab;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import jp.co.soramitsu.inport.R;

public class CommonPictureAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public CommonPictureAdapter(List<String> data) {
        super(R.layout.fragment_home_theme_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.picture_name, item);
    }
}
