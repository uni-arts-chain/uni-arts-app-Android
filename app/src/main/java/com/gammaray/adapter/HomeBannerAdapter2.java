package com.gammaray.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.base.YunApplication;
import com.gammaray.entity.ArtAuctionVo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Synopsis     com.miner.client.adapter.BannerAdapter
 * Author		Mosr
 * Version		1.0.0
 * Create 	    2020-06-20 14:15:06
 * Email  		intimatestranger@sina.cn
 */
public class HomeBannerAdapter2 extends BaseQuickAdapter<ArtAuctionVo, BaseViewHolder> {

    SimpleDateFormat simpleDateFormat;

    public HomeBannerAdapter2(int layoutResId, @Nullable List<ArtAuctionVo> data) {
        super(layoutResId, data);
        simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
    }

    @Override
    protected void convert(BaseViewHolder helper, ArtAuctionVo item) {
        Date date = new Date(Long.parseLong(item.getStart_at()) * 1000);
        Date endDate = new Date(Long.parseLong(item.getEnd_at()) * 1000);
        helper.setText(R.id.artist_time, simpleDateFormat.format(date) + " - " + simpleDateFormat.format(endDate));
        helper.setText(R.id.artist_amount, String.valueOf(item.getArt_size()).concat("件"));
        Glide.with(YunApplication.getInstance())
                .load(item.getImg_file().getUrl())
                .into((ImageView) helper.getView(R.id.artist_pic));
    }

}
