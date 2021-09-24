package com.gammaray.ui.activity

import android.text.TextUtils
import android.view.KeyEvent
import com.blankj.utilcode.util.ToastUtils
import com.gammaray.R
import com.gammaray.base.BaseActivity
import com.gammaray.databinding.ActivityPinCodeKtBinding
import com.gammaray.ui.activity.wallet.WalletEditActivity
import com.gammaray.utils.SharedPreUtils

class PinCodeKtActivity : BaseActivity<ActivityPinCodeKtBinding>() {
    var firstPsw: String? = null

    private var resumeCer = true
    private var reset = false
    private var set = false
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
        } else if (set) {
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
        var setPsw = intent.getBooleanExtra(WalletEditActivity.SET_CER, false)
        resumeCer = !reset
        set = setPsw
        with(mDataBinding.pinCodeView) {
            pinCodeEnteredListener = { pinCodeEntered(it) }
        }
        mDataBinding.cancle.setOnClickListener({ finish() })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (set)
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true
            }
        return super.onKeyDown(keyCode, event)
    }
}