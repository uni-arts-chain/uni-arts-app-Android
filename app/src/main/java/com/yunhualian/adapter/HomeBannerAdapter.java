package com.yunhualian.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.ArtAuctionVo;
import com.yunhualian.entity.BannersVo;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Synopsis     com.miner.client.adapter.BannerAdapter
 * Author		Mosr
 * Version		1.0.0
 * Create 	    2020-06-20 14:15:06
 * Email  		intimatestranger@sina.cn
 */
public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.ViewHolder> {


    private List<ArtAuctionVo> list;
    private OnItemClickListener mOnItemClickListener;

    private Context mContext;
    private WeakReference<ColorDrawable> mErrorDrawable;

    public HomeBannerAdapter(Context mContext, List<ArtAuctionVo> list) {
        this.list = list;
        this.mContext = mContext;
        mErrorDrawable = new WeakReference<>(new ColorDrawable(YunApplication.getInstance().getResources().getColor(R.color._CCCCCC)));
    }

    public void setList(List<ArtAuctionVo> list) {
        this.list = list;
    }

    public OnItemClickListener getmOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_banner_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (null == holder) {
            return;
        }
        if (null == holder.imageView) {
            return;
        }
        if (null == list || list.isEmpty()) {
            return;
        }
        final int mRealPostion;
        ArtAuctionVo mBannersVo = list.get(mRealPostion = (holder.getAdapterPosition() % list.size()));


        Glide.with(YunApplication.getInstance())
                .load(mBannersVo.getImg_file().getUrl())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onClick(holder, list, "mBannersVo.getUrl()", mRealPostion);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.artist_pic);
        }

    }

    public interface OnItemClickListener {
        void onClick(ViewHolder holder, List<ArtAuctionVo> list, String mUrl, int position);
    }
}
