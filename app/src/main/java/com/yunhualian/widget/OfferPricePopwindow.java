package com.yunhualian.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.yunhualian.R;
import com.yunhualian.databinding.PopOfferPriceLayoutBinding;
import com.yunhualian.databinding.PopPasswdLayoutBinding;


public class OfferPricePopwindow extends BasePopupWindow {
    private PopOfferPriceLayoutBinding mBinding;
    private OnBottomTextviewClickListener listener;

    public OfferPricePopwindow(Context context, OnBottomTextviewClickListener listener) {
        super(context);
        this.listener = listener;
    }


    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

//        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.pop_offer_price_layout, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
        mBinding.setHandlers(new ClickHandlers());
        mBinding.confirm.setOnClickListener(v -> {
            listener.onPerformClick();
        });
    }

    public interface OnPopItemClickListener {
        void onPopItemClick(View view, int position);
    }

    public interface OnBottomTextviewClickListener {
        void onCancleClick();

        void onPerformClick();
    }

    public class ClickHandlers {

        public void onClickDeposit() {
            dismiss();

        }

        public void onClickWithdraw() {
            dismiss();
        }

    }

    public void setContent(String str) {

    }

    public void setConfirmText(String string) {
        if (!TextUtils.isEmpty(string))
            mBinding.confirm.setText(string);
    }
}
