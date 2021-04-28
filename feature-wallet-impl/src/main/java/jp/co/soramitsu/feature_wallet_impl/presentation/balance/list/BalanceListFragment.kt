package jp.co.soramitsu.feature_wallet_impl.presentation.balance.list

import android.os.Bundle
import android.util.EventLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.upbest.arouter.EventBusMessageEvent
import com.upbest.arouter.EventEntity
import jp.co.soramitsu.common.base.BaseFragment
import jp.co.soramitsu.common.di.FeatureUtils
import jp.co.soramitsu.feature_wallet_api.di.WalletFeatureApi
import jp.co.soramitsu.feature_wallet_impl.R
import jp.co.soramitsu.feature_wallet_impl.di.WalletFeatureComponent
import jp.co.soramitsu.feature_wallet_impl.presentation.balance.assetActions.setupBuyIntegration
import jp.co.soramitsu.feature_wallet_impl.presentation.balance.list.changeAccount.AccountChooserBottomSheetDialog
import jp.co.soramitsu.feature_wallet_impl.presentation.model.AssetModel
import jp.co.soramitsu.feature_wallet_impl.util.format
import jp.co.soramitsu.feature_wallet_impl.util.formatAsCurrency
import kotlinx.android.synthetic.main.fragment_balance_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class BalanceListFragment : BaseFragment<BalanceListViewModel>(), BalanceListAdapter.ItemAssetHandler {

    private lateinit var adapter: BalanceListAdapter
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
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        adapter = BalanceListAdapter(this)
        balanceListAssets.adapter = adapter

        transfersContainer.initializeBehavior(anchorView = balanceListContent)

        transfersContainer.setScrollingListener(viewModel::scrolled)

        headLayout.setOnClickListener {
            EventBus.getDefault().postSticky(EventBusMessageEvent("", addr))
            viewModel.headClicked()
        }
        pointLayout.setOnClickListener { viewModel.sendClicked() }
        transfersContainer.setSlidingStateListener(this::setRefreshEnabled)

        transfersContainer.setTransactionClickListener(viewModel::transactionClicked)

        walletContainer.setOnRefreshListener {
            viewModel.refresh()
        }
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
    }

    private fun setRefreshEnabled(bottomSheetState: Int) {
        val bottomSheetCollapsed = BottomSheetBehavior.STATE_COLLAPSED == bottomSheetState
        walletContainer.isEnabled = bottomSheetCollapsed
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
            balance.text = it.assetModels[0].total.format()

//            EventBus.getDefault().post(EventBusMessageEvent(EventEntity.EVENT_BLLANCE_REFRESH, it.totalBalance.formatAsCurrency()))
        }

        viewModel.currentAddressModelLiveData.observe {
            balanceListAvatar.setImageDrawable(it.image)
            addr.text = it.address
            name.text = it.name
            viewModel.openSeed()
        }

        viewModel.hideRefreshEvent.observeEvent {
            walletContainer.isRefreshing = false
        }


        viewModel.showAccountChooser.observeEvent {
            AccountChooserBottomSheetDialog(requireActivity(), it, viewModel::accountSelected).show()

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
}