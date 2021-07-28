package com.yunhualian.ui.activity

import com.yunhualian.R
import com.yunhualian.base.BaseActivity
import com.yunhualian.base.ToolBarOptions
import com.yunhualian.databinding.ActivityCashAccountLayoutBinding
import com.yunhualian.ui.activity.wallet.WithdrawActivity

/*
* 现金账户 Activity
* */
class CashAccountActivity : BaseActivity<ActivityCashAccountLayoutBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_cash_account_layout
    }

    override fun initPresenter() {

    }

    override fun initView() {
        val toolBarOptions = ToolBarOptions()
        toolBarOptions.titleId = R.string.cash_account_
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, toolBarOptions)

        mDataBinding.btnWithdraw.setOnClickListener { startActivity(WithdrawActivity::class.java) }
    }
}