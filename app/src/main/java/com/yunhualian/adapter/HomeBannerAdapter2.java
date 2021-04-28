package com.yunhualian.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.ArtAuctionVo;

import java.lang.ref.WeakReference;
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
