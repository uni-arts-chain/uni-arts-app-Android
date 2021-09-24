package com.gammaray.ui.activity

import android.content.Intent
import android.text.TextUtils
import android.view.KeyEvent
import com.blankj.utilcode.util.ToastUtils
import com.gammaray.R
import com.gammaray.base.BaseActivity
import com.gammaray.databinding.ActivityEthPinCodeLayoutBinding
import com.gammaray.databinding.ActivityPinCodeKtBinding
import com.gammaray.eth.util.Md5Utils
import com.gammaray.ui.activity.wallet.WalletEditActivity
import com.gammaray.utils.SharedPreUtils
import org.bouncycastle.jcajce.provider.digest.MD5

class ETHPinCodeActivity : BaseActivity<ActivityEthPinCodeLayoutBinding>() {
    var firstPsw: String? = null

    private var resumeCer = true
    private var reset = false
    private var set = false
    var psw: String? = null
    var oldpsw: String? = null
    fun pinCodeEntered(code: String) {

        if (resumeCer) {
            if (Md5Utils.md5(code).equals(psw)) {
                //验证通过
                val intent = Intent()
                intent.putExtra("input_pwd", code)
                setResult(1, intent)
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
                setResult(0)
                finish()
            } else {
                firstPsw = ""
                mDataBinding.pinCodeView.setTitle("两次密码不一致，请重新输入")
                mDataBinding.pinCodeView.resetInput()
            }
        } else {//重设密码
            val intent = Intent()
            if (Md5Utils.md5(code).equals(psw) && !reset) {
                oldpsw = code
                mDataBinding.pinCodeView.setTitle("请输入新密码")
                mDataBinding.pinCodeView.resetInput()
                reset = true
            } else if (reset) {
                if (TextUtils.isEmpty(firstPsw)) {
                    firstPsw = code
                    mDataBinding.pinCodeView.setTitle("请再次输入密码")
                    mDataBinding.pinCodeView.resetInput()
                } else if (firstPsw.equals(code)) {
                    intent.putExtra("input_old_pwd", oldpsw)
                    intent.putExtra("input_new_pwd", code)
                    setResult(1, intent)
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
        return R.layout.activity_eth_pin_code_layout
    }

    override fun initPresenter() {
    }

    override fun initView() {
        mDataBinding.pinCodeView.setTitle("请输入密码")
        psw = intent.getStringExtra("wallet_pwd")
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