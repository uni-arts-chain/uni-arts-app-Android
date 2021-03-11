package jp.co.soramitsu.app.root.presentation.tab;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import jp.co.soramitsu.inport.R;

public class HomePagePopularAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public HomePagePopularAdapter(List<String> data) {
        super(R.layout.fragment_pictures_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int position = helper.getPosition();
        ImageView imageView = helper.getView(R.id.hot_picture);
        imageView.setBackgroundResource(R.mipmap.shanshui);
//        RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(30));//图片圆角为30
//
//        Glide.with(mContext).load(mContext.getResources().getDrawable(R.mipmap.shanshui)) //图片地址
//                .apply(options)
//                .into(imageView);
    }
}
