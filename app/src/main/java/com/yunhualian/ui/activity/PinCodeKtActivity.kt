package com.yunhualian.ui.activity

import android.text.TextUtils
import com.blankj.utilcode.util.ToastUtils
import com.yunhualian.R
import com.yunhualian.base.BaseActivity
import com.yunhualian.databinding.ActivityPinCodeKtBinding
import com.yunhualian.ui.activity.wallet.WalletEditActivity
import com.yunhualian.utils.SharedPreUtils

class PinCodeKtActivity : BaseActivity<ActivityPinCodeKtBinding>() {
    var firstPsw: String? = null

    private var resumeCer = true
    private var reset = false
    var psw: String? = null
    fun pinCodeEntered(code: String) {

        if (resumeCer) {
            if (code.equals(psw)) {
                //验证通过
                setResult(1)
                finish()
            } else {
                mDataBinding.pinCodeView.setTitle("密码错误")
                mDataBinding.pinCodeView.resetInput()
            }
        } else {//重设密码
            if (code.equals(psw) && !reset) {
                mDataBinding.pinCodeView.setTitle("请输入新密码")
                mDataBinding.pinCodeView.resetInput()
                reset = true
            } else if (reset) {
                if (TextUtils.isEmpty(firstPsw)) {
                    firstPsw = code
                    mDataBinding.pinCodeView.setTitle("请再次输入密码")
                    mDataBinding.pinCodeView.resetInput()
                } else if (firstPsw.equals(code)) {
                    SharedPreUtils.setString(this, SharedPreUtils.KEY_PIN, code)
                    ToastUtils.showShort("密码重置完成")
                    finish()
                } else {
                    firstPsw = ""
                    mDataBinding.pinCodeView.setTitle("两次密码不一致，请重新输入")
                    mDataBinding.pinCodeView.resetInput()
                }
            } else {
                mDataBinding.pinCodeView.setTitle("密码错误")
                mDataBinding.pinCodeView.resetInput()
            }

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_pin_code_kt
    }

    override fun initPresenter() {
    }

    override fun initView() {
        mDataBinding.pinCodeView.setTitle("请输入密码")
        psw = SharedPreUtils.getString(this, SharedPreUtils.KEY_PIN)
        var reset = intent.getBooleanExtra(WalletEditActivity.RESUME_CER, false)
        resumeCer = !reset
        with(mDataBinding.pinCodeView) {
            pinCodeEnteredListener = { pinCodeEntered(it) }
        }
        mDataBinding.cancle.setOnClickListener({ finish() })
    }
}