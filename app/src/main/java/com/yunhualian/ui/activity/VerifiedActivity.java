package com.yunhualian.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.sensetime.liveness.motion.ImageManager;
import com.sensetime.liveness.motion.MotionLivenessActivity;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.constant.ExtraConstant;
import com.yunhualian.databinding.ActivityVerifiedBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.utils.IDCardInfoExtractor;
import com.yunhualian.utils.ToastManager;

import retrofit2.Call;
import retrofit2.Response;

public class VerifiedActivity extends BaseActivity<ActivityVerifiedBinding> {


    private static final int REQUEST_CODE_LIVENESS = 0x1001;
    private String mActualName;
    private String mIDNumber;

    @Override
    public int getLayoutId() {
        return R.layout.activity_verified;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.physical_authentication;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

    }

    public void onClickStartPhysicalAuthentication(View view) {
        mActualName = mDataBinding.edtAvActualName.getText().toString().trim();
        mIDNumber = mDataBinding.edtAvIDNumber.getText().toString().trim().toUpperCase();

        if (TextUtils.isEmpty(mActualName) || TextUtils.isEmpty(mIDNumber)) {
            ToastManager.showShort(R.string.hint_please_input_id_info);
            return;
        }
        IDCardInfoExtractor mIDCardInfoExtractor = new IDCardInfoExtractor();
        int mCheckResult = mIDCardInfoExtractor.parse(mIDNumber);
        if (mCheckResult != 1) {
            ToastManager.showShort(mCheckResult == 0 ? R.string.hint_please_input_id_info : R.string.hint_id_number_illegal);
            return;
        }
        if (mIDCardInfoExtractor.getAge() < 18) {
            ToastManager.showShort(R.string.hint_certification_must_be_over_18_years_old);
            return;
        }
        final Intent intent = new Intent(VerifiedActivity.this, MotionLivenessActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LIVENESS);
        //idCardsCheck();
    }

    private void idCardsCheck() {
        showLoading(R.string.progress_loading);
        RequestManager.instance().idCardsCheck(mActualName, mIDNumber, new MinerCallback<BaseResponseVo>() {
            @Override
            public void onSuccess(Call<BaseResponseVo> call, Response<BaseResponseVo> response) {
                if (response.body().isSuccessful()) {
                    final Intent intent = new Intent(VerifiedActivity.this, MotionLivenessActivity.class);
        /* intent.putExtra(MotionLivenessActivity.EXTRA_DIFFICULTY, PreferenceUtil.getDifficulty());
            intent.putExtra(MotionLivenessActivity.EXTRA_VOICE, PreferenceUtil.isInteractiveLivenessVoiceOn());
            intent.putExtra(MotionLivenessActivity.EXTRA_SEQUENCES, PreferenceUtil.getSequence());*/
                    startActivityForResult(intent, REQUEST_CODE_LIVENESS);
//        startDetectActivity(0);
                } else {
                    if (null != response.body().getHead())
                        ToastManager.showShort(response.body().getHead().getMsg());
                }
            }

            @Override
            public void onError(Call<BaseResponseVo> call, Response<BaseResponseVo> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            boolean bDealError = data.getBooleanExtra(ExtraConstant.RESULT_DEAL_ERROR_INNER, true);
            final byte[] mResult = ImageManager.getInstance().getResult();

            if (bDealError || null == mResult || mResult.length == 0) {
//                ToastManager.showShort(R.string.hint_liveness_verify_failed);
                return;
            }

            Bundle mBundle = null != getIntent() && null != getIntent().getExtras() ? getIntent().getExtras() : new Bundle();
            mBundle.putString(ExtraConstant.KEY_ACTUAL_NAME, mActualName);
            mBundle.putString(ExtraConstant.KEY_ID_NUMBER, mIDNumber);
            startActivity(ConfirmDocumentsActivity.class, mBundle);
        } else {
//            ToastManager.showShort(R.string.hint_liveness_verify_failed);
            LogUtils.iTag("mosr", "data = null");
        }
    }
}