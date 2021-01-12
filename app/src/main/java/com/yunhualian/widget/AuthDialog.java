package com.yunhualian.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yunhualian.R;
import com.yunhualian.base.YunApplication;
import com.yunhualian.ui.activity.VerifiedActivity;


/**
 * Author       hbc
 * Version      1.0.0
 * Create       2020/7/15 9:21
 * Email
 * <p>
 * new AuthDialog(this).setClickListenerInterface(new AuthDialog.ClickListenerInterface() {
 * //            @Override
 * //            public void doConfirm() {
 * //              确定按钮点击监听，默认取消对话框
 * //            }
 * //        }).setCompleteAuthListener(new AuthDialog.CompleteAuthListener() {
 * //            @Override
 * //            public void doComplete() {
 * //               完成认证业务逻辑执行
 * //            }
 * //        }).isNeedShow();
 */


public class AuthDialog extends Dialog {
    private Context mContext;
    private boolean mTwoPartState;//是否绑定了两部验证
    private boolean mTealNameState;//是否实名认证
    private ClickListenerInterface clickListenerInterface;//确定按钮点击监听
    private CompleteAuthListener CompleteAuthListener;//完成认证监听

    public AuthDialog(Context mContext) {
        super(mContext, R.style.MyDialogStyle);
        this.mContext = mContext;
        setState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {

        setContentView(R.layout.dialog_auth_layout);

        TextView mTwoPartVerifiTv = findViewById(R.id.tv_two_part_verifi);
        TextView mRealNameAuthTv = findViewById(R.id.tv_real_name_auth);


        mTwoPartVerifiTv.setText(mTwoPartState ? R.string.certified : R.string.go_certify);
        mRealNameAuthTv.setText(mTealNameState ? R.string.certified : R.string.go_certify);

        mTwoPartVerifiTv.setBackgroundResource(mTwoPartState ? R.drawable.rounded_rect_gray2 : R.drawable.rounded_rect_theme);
        mRealNameAuthTv.setBackgroundResource(mTealNameState ? R.drawable.rounded_rect_gray2 : R.drawable.rounded_rect_theme);

        if (!mTwoPartState)
            mTwoPartVerifiTv.setOnClickListener(new DialogClickListener());

        if (!mTealNameState)
            mRealNameAuthTv.setOnClickListener(new DialogClickListener());

        findViewById(R.id.tv_i_see).setOnClickListener(new DialogClickListener());

        setWidthScale(0.8);
    }

    /**
     * 设置宽度比例
     *
     * @param scale 比例
     * @return AuthDialog
     */
    public AuthDialog setWidthScale(double scale) {
        Window dialogWindow = getWindow();
        if (null != dialogWindow) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = mContext.getResources().getDisplayMetrics();
            lp.width = (int) (d.widthPixels * scale);
            dialogWindow.setAttributes(lp);
        }
        return this;
    }

    private void setState() {
        mTwoPartState = YunApplication.getmUserVo().isApp_validated();
        mTealNameState = YunApplication.getmUserVo().isId_document_validated();
    }

    /**
     * 是否完成认证
     *
     * @return 是否完成认证
     */
    public boolean isCompleteAuth() {
        return mTwoPartState && mTealNameState;
        //   return true;
    }

    /**
     * 完成认证监听 处理业务逻辑
     *
     * @param completeAuthListener completeAuthListener
     */
    public AuthDialog setCompleteAuthListener(CompleteAuthListener completeAuthListener) {
        this.CompleteAuthListener = completeAuthListener;
        return this;
    }

    /**
     * 确定按钮点击监听
     *
     * @param clickListenerInterface clickListenerInterface
     * @return AuthDialog
     */
    public AuthDialog setClickListenerInterface(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
        return this;
    }

    public AuthDialog isNeedShow() {
        if (!isCompleteAuth()) {
            super.show();
        } else {
            if (null != CompleteAuthListener) {
                dismiss();
                CompleteAuthListener.doComplete();
            }
        }
        return this;
    }

    private class DialogClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_i_see:
                    if (null != clickListenerInterface) {
                        clickListenerInterface.doConfirm();
                        dismiss();
                    }
                    dismiss();
                    break;
                case R.id.tv_two_part_verifi:
                case R.id.tv_real_name_auth:
                    mContext.startActivity(new Intent(mContext, VerifiedActivity.class));
                    dismiss();
                    break;
            }
        }
    }

    public interface ClickListenerInterface {
        void doConfirm();
    }

    public interface CompleteAuthListener {
        void doComplete();
    }

}
