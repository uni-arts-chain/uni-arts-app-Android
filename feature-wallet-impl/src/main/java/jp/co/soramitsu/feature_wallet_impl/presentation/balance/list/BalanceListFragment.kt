package jp.co.soramitsu.feature_wallet_impl.presentation.balance.list

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.upbest.arouter.ArouterModelPath
import com.upbest.arouter.EventBusMessageEvent
import com.upbest.arouter.EventEntity
import com.upbest.arouter.Extras
import jp.co.soramitsu.common.base.BaseFragment
import jp.co.soramitsu.common.di.FeatureUtils
import jp.co.soramitsu.common.utils.setVisible
import jp.co.soramitsu.feature_wallet_api.di.WalletFeatureApi
import jp.co.soramitsu.feature_wallet_impl.R
import jp.co.soramitsu.feature_wallet_impl.di.WalletFeatureComponent
import jp.co.soramitsu.feature_wallet_impl.presentation.balance.MyHomePageAdapter
import jp.co.soramitsu.feature_wallet_impl.presentation.balance.assetActions.setupBuyIntegration
import jp.co.soramitsu.feature_wallet_impl.presentation.balance.list.changeAccount.AccountChooserBottomSheetDialog
import jp.co.soramitsu.feature_wallet_impl.presentation.model.AssetModel
import jp.co.soramitsu.feature_wallet_impl.util.formatAsCurrency
import kotlinx.android.synthetic.main.fragment_balance_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.collections.ArrayList

class BalanceListFragment : BaseFragment<BalanceListViewModel>(),
    BalanceListAdapter.ItemAssetHandler, MyHomePageAdapter.TabPagerListener {
    private lateinit var adapter: BalanceListAdapter
    private val walletLinks: ArrayList<WalletTokenBean> = ArrayList()
    var address: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_balance_list, container, false)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: EventBusMessageEvent) {

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun initViews() {

        noLayut.setVisible(!Extras.isShow)
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        adapter = BalanceListAdapter(this)
        balanceListAssets.adapter = adapter

        transfersContainer.initializeBehavior(anchorView = balanceListContent)

        transfersContainer.setScrollingListener(viewModel::scrolled)

        img_setting.setOnClickListener {
//            EventBus.getDefault().postSticky(EventBusMessageEvent(EventEntity.EVENT_ADDRESS_COPY, addr))
//            viewModel.headClicked()

            ARouter.getInstance().build(ArouterModelPath.EDIT_WALLET).navigation()
        }
        addr.setOnClickListener {
//            EventBus.getDefault().postSticky(EventBusMessageEvent(EventEntity.EVENT_ADDRESS_COPY, addr.text))
        }
        copyAddress.setOnClickListener {
            EventBus.getDefault()
                .postSticky(EventBusMessageEvent(EventEntity.EVENT_ADDRESS_COPY, addr.text))
        }
        showQr.setOnClickListener {
            EventBus.getDefault()
                .postSticky(EventBusMessageEvent(EventEntity.EVENT_SHOW_QR, addr.text))
        }
//        btn_import_wallet.setOnClickListener {
//            EventBus.getDefault()
//                .postSticky(EventBusMessageEvent(EventEntity.EVENT_IMPORT_WALLET, addr.text))
//        }
        pointLayout.setOnClickListener { viewModel.sendClicked() }
        transfersContainer.setSlidingStateListener(this::setRefreshEnabled)

        transfersContainer.setTransactionClickListener(viewModel::transactionClicked)

//        walletContainer.setOnRefreshListener {
//            viewModel.refresh()
//        }
//        toolbar.setHomeButtonListener { viewModel.homeButtonClicked() }
        balanceListActions.send.setOnClickListener {
            viewModel.sendClicked()
        }

        balanceListActions.receive.setOnClickListener {
            viewModel.receiveClicked()
        }

        balanceListActions.buy.setOnClickListener {
            viewModel.buyClicked()
        }

        balanceListAvatar.setOnClickListener {
            viewModel.avatarClicked()
        }
        if (!TextUtils.isEmpty(Extras.balance)) {
            balance.text = Extras.balance
            walletLinks.add(
                WalletTokenBean(
                    "UART",
                    "UniArt",
                    R.drawable.icon_uart,
                    Extras.balance
                )
            )
        } else {
            walletLinks.add(
                WalletTokenBean(
                    "UART",
                    "UniArt",
                    R.drawable.icon_uart,
                    "0"
                )
            )
        }

        if (!TextUtils.isEmpty(Extras.headUrl))
            Glide.with(this).load(Extras.headUrl).into(imageview)

        val mAdapter = MyHomePageAdapter(
            childFragmentManager,
            2,
            Arrays.asList(*resources.getStringArray(R.array.personal_tabs)),
            requireContext()
        )
        mAdapter.setListener(this)
        viewpager.setAdapter(mAdapter)
        tabLayout.setupWithViewPager(viewpager)
        tabLayout.setTabMode(TabLayout.MODE_FIXED)
    }

    private fun setRefreshEnabled(bottomSheetState: Int) {
        val bottomSheetCollapsed = BottomSheetBehavior.STATE_COLLAPSED == bottomSheetState
//        walletContainer.isEnabled = bottomSheetCollapsed
    }

    override fun inject() {
        FeatureUtils.getFeature<WalletFeatureComponent>(
            requireContext(),
            WalletFeatureApi::class.java
        )
            .balanceListComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun subscribe(viewModel: BalanceListViewModel) {
        viewModel.syncAssetsRates()
        viewModel.syncFirstTransactionsPage()

        setupBuyIntegration(viewModel)

        viewModel.transactionsLiveData.observe(transfersContainer::showTransactions)

        viewModel.buyEnabledLiveData.observe(balanceListActions.buy::setEnabled)

        viewModel.balanceLiveData.observe {
            adapter.submitList(it.assetModels)
            balanceListTotalAmount.text = it.totalBalance.formatAsCurrency()
//            balance.text = it.assetModels[0].total.format()
        }

        viewModel.currentAddressModelLiveData.observe {
            balanceListAvatar.setImageDrawable(it.image)
            addr.text = it.address
            name.text = it.name
            viewModel.openSeed()
        }

        viewModel.hideRefreshEvent.observeEvent {
//            walletContainer.isRefreshing = false
        }


        viewModel.showAccountChooser.observeEvent {
            AccountChooserBottomSheetDialog(
                requireActivity(),
                it,
                viewModel::accountSelected
            ).show()

        }
    }

    override fun assetClicked(asset: AssetModel) {
        viewModel.assetClicked(asset)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(eventBusMessageEvent: EventBusMessageEvent) {
//        Toast.makeText(requireContext(), eventBusMessageEvent.getmValue().toString(), Toast.LENGTH_LONG).show()
//        if (eventBusMessageEvent.getmMessage() == EventEntity.EVENT_GO_ACOUNT) {
//            Toast
//        }
    }

    override fun getFragment(position: Int): Fragment {
        if (position == 0) {
            return PersonalAssertFragment.newInstance("TOKEN", walletLinks)
        } else {
            return PersonalAssertFragment.newInstance("NFT", walletLinks)
        }
    }
}