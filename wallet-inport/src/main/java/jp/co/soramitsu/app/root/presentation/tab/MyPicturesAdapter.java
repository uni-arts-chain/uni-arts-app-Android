package jp.co.soramitsu.app.root.presentation.tab;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import jp.co.soramitsu.inport.R;

public class MyPicturesAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MyPicturesAdapter(List<String> data) {
        super(R.layout.fragment_my_pictures_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int position = helper.getPosition();
        ImageView ivImage = helper.getView(R.id.picture);
    }
}
