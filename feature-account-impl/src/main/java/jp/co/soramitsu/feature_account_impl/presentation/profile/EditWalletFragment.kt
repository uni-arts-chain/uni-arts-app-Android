package jp.co.soramitsu.feature_account_impl.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import jp.co.soramitsu.common.account.external.actions.ExternalAccountActions
import jp.co.soramitsu.common.account.external.actions.copyAddressClicked
import jp.co.soramitsu.common.base.BaseFragment
import jp.co.soramitsu.common.di.FeatureUtils
import jp.co.soramitsu.common.mixin.impl.observeBrowserEvents
import jp.co.soramitsu.common.view.MnemonicBackupAlertDialog
import jp.co.soramitsu.common.view.PrivateKeyBackupAlertDialog
import jp.co.soramitsu.feature_account_api.di.AccountFeatureApi
import jp.co.soramitsu.feature_account_impl.R
import jp.co.soramitsu.feature_account_impl.di.AccountFeatureComponent
import kotlinx.android.synthetic.main.fragment_backup_mnemonic.*
import kotlinx.android.synthetic.main.fragment_edit_wallet.*
import kotlinx.android.synthetic.main.fragment_export_seed.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.aboutTv
import kotlinx.android.synthetic.main.fragment_profile.accountView
import kotlinx.android.synthetic.main.fragment_profile.changePinCodeTv
import kotlinx.android.synthetic.main.fragment_profile.languageWrapper
import kotlinx.android.synthetic.main.fragment_profile.networkWrapper
import kotlinx.android.synthetic.main.fragment_profile.profileAccounts
import kotlinx.android.synthetic.main.fragment_profile.selectedLanguageTv
import kotlinx.android.synthetic.main.fragment_profile.selectedNetworkTv
import kotlinx.android.synthetic.main.fragment_profile.toolbar

private const val ACCOUNT_ADDRESS_KEY = "ACCOUNT_ADDRESS_KEY"

class EditWalletFragment : BaseFragment<EditWalletViewModel>() {

    var seedString = ""

    companion object {
        var pincodeCheck: Boolean = false
        fun getBundle(accountAddress: String): Bundle {
            return Bundle().apply {
                putString(ACCOUNT_ADDRESS_KEY, accountAddress)
            }
        }
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_wallet, container, false)
    }

    override fun initViews() {
        accountView.setWholeClickListener { viewModel.accountActionsClicked() }
        toolbar.setTitle(getString(R.string.wallet_addr))
        aboutTv.setOnClickListener { viewModel.aboutClicked() }

        profileAccounts.setOnClickListener { viewModel.accountsClicked() }
        networkWrapper.setOnClickListener {
//            showToast()
            viewModel.networksClicked()
        }
        languageWrapper.setOnClickListener { viewModel.languagesClicked() }
        changePinCodeTv.setOnClickListener { viewModel.changePinCodeClicked() }
    }

    override fun onResume() {
        super.onResume()
        if (pincodeCheck) {
            showToast()
            pincodeCheck = false
        }
    }

    override fun inject() {
        val accountAddress = argument<String>(ACCOUNT_ADDRESS_KEY)
        FeatureUtils.getFeature<AccountFeatureComponent>(
                requireContext(),
                AccountFeatureApi::class.java
        )
                .editWalletComponentFactory()
                .create(this, accountAddress)
                .inject(this)
    }

    override fun subscribe(viewModel: EditWalletViewModel) {
        observeBrowserEvents(viewModel)

        viewModel.selectedAccountLiveData.observe { account ->
            account.name?.let(walletName::setText)

            accountView.setText(account.address)

            selectedNetworkTv.text = account.network.name
        }

        viewModel.accountIconLiveData.observe {
            imageview.setImageDrawable(it.image)
        }

        viewModel.selectedLanguageLiveData.observe {
            selectedLanguageTv.text = it.displayName
        }
        viewModel.seedLiveData.observe {
//            exportSeedValue.setMessage(it)
//            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            seedString = it;
        }

        viewModel.showExternalActionsEvent.observeEvent(::showAccountActions)
    }

    private fun showAccountActions(payload: ExternalAccountActions.Payload) {
        ProfileActionsSheet(
                requireContext(),
                payload,
                viewModel::copyAddressClicked,
                viewModel::viewExternalClicked,
                viewModel::accountsClicked
        ).show()
    }

    fun showToast() {
//        Toast.makeText(requireContext(), seedString, Toast.LENGTH_LONG).show()

        val mnemonicBackupAlertDialog = PrivateKeyBackupAlertDialog(requireContext(), seedString, View.OnClickListener {
            if (it.id == R.id.btn_confirm)
                Toast.makeText(requireContext(), "复制成功", Toast.LENGTH_LONG).show()
        })
        mnemonicBackupAlertDialog.show()

    }
}

