package com.yunhualian.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.databinding.PopupwindowAssetsOperatingBinding;
import com.yunhualian.databinding.PopupwindowQrCordeBinding;
import com.yunhualian.databinding.PopupwindowSelectorOperatingBinding;

import java.util.List;


public class QrPopUpWindow extends BasePopupWindow {
    private PopupwindowQrCordeBinding mBinding;

    public QrPopUpWindow(Context context) {
        super(context);
    }


    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_qr_corde, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

    }

    public interface OnPopItemClickListener {
        void onPopItemClick(View view, int position);
    }

    public interface OnBottomTextviewClickListener {
        void onBottomClick();
    }

    public void setImage(Bitmap bitmap) {
        mBinding.img.setImageBitmap(bitmap);
    }

    public class ClickHandlers {

        public void onClickDeposit() {
            dismiss();

        }

        public void onClickWithdraw() {
            dismiss();

        }

        public void onClickTransfer() {
            dismiss();
        }
    }

    public class PopWindowListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public PopWindowListAdapter(@Nullable List<String> data) {
            super(R.layout.pop_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.itemText, item).addOnClickListener(R.id.itemText);
        }

    }
}
