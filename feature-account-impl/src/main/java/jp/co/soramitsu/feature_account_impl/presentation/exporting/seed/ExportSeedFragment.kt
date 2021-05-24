package jp.co.soramitsu.feature_account_impl.presentation.exporting.seed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.upbest.arouter.ArouterModelPath
import com.upbest.arouter.EventBusMessageEvent
import com.upbest.arouter.EventEntity
import com.upbest.arouter.Extras
import jp.co.soramitsu.common.di.FeatureUtils
import jp.co.soramitsu.feature_account_api.di.AccountFeatureApi
import jp.co.soramitsu.feature_account_impl.R
import jp.co.soramitsu.feature_account_impl.di.AccountFeatureComponent
import jp.co.soramitsu.feature_account_impl.presentation.exporting.ExportFragment
import jp.co.soramitsu.feature_account_impl.presentation.view.advanced.AdvancedBlockView.FieldState
import kotlinx.android.synthetic.main.fragment_export_seed.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

private const val ACCOUNT_ADDRESS_KEY = "ACCOUNT_ADDRESS_KEY"

class ExportSeedFragment : ExportFragment<ExportSeedViewModel>() {

    companion object {
        fun getBundle(accountAddress: String): Bundle {
            return Bundle().apply {
                putString(ACCOUNT_ADDRESS_KEY, accountAddress)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_export_seed, container, false)
    }

    override fun initViews() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        exportSeedToolbar.setHomeButtonListener { viewModel.back() }

        configureAdvancedBlock()

        exportSeedExport.setOnClickListener { viewModel.exportClicked() }
    }

    private fun configureAdvancedBlock() {
        with(exportSeedAdvanced) {
            configure(FieldState.DISABLED)
        }
    }

    var accountAddress: String = ""
    override fun inject() {
        accountAddress = argument(ACCOUNT_ADDRESS_KEY)

        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
                .exportSeedFactory()
                .create(this, accountAddress)
                .inject(this)
    }

    override fun subscribe(viewModel: ExportSeedViewModel) {
        super.subscribe(viewModel)

        val typeNameRes = viewModel.exportSource.nameRes

        exportSeedType.setMessage(typeNameRes)

        viewModel.derivationPathLiveData.observe {
            val state = if (it.isNullOrBlank()) FieldState.HIDDEN else FieldState.DISABLED

            with(exportSeedAdvanced) {
                configure(derivationPathField, state)

                setDerivationPath(it)
            }
        }

        viewModel.seedLiveData.observe {
            Extras.seed = it
            EventBus.getDefault().postSticky(EventBusMessageEvent(EventEntity.EVENT_BLLANCE_REFRESH, it))
            exportSeedValue.setMessage(it)

            ARouter.getInstance().build(ArouterModelPath.MAIN).navigation()
            EventBus.getDefault().post(EventBusMessageEvent(EventEntity.EVENT_FINISH, ""))
            activity?.finish()
//            viewModel.openMain()
        }

        viewModel.cryptoTypeLiveData.observe {
            exportSeedAdvanced.setEncryption(it.name)
        }

        viewModel.networkTypeLiveData.observe {
            exportSeedAdvanced.setNetworkName(it.name)
            exportSeedAdvanced.setNetworkIconResource(it.networkTypeUI.icon)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(eventBusMessageEvent: EventBusMessageEvent) {
//        if (eventBusMessageEvent.getmMessage() == EventEntity.EVENT_GO_ACOUNT) {
//            Toast
//        }
    }
}