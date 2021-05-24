package jp.co.soramitsu.feature_account_impl.presentation.account.details

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import com.bumptech.glide.Glide
import com.upbest.arouter.EventBusMessageEvent
import com.upbest.arouter.EventEntity
import com.upbest.arouter.Extras
import jp.co.soramitsu.common.account.external.actions.setupExternalActions
import jp.co.soramitsu.common.base.BaseFragment
import jp.co.soramitsu.common.di.FeatureUtils
import jp.co.soramitsu.common.utils.nameInputFilters
import jp.co.soramitsu.common.utils.onTextChanged
import jp.co.soramitsu.common.view.bottomSheet.list.dynamic.DynamicListBottomSheet.Payload
import jp.co.soramitsu.feature_account_api.di.AccountFeatureApi
import jp.co.soramitsu.feature_account_impl.R
import jp.co.soramitsu.feature_account_impl.di.AccountFeatureComponent
import jp.co.soramitsu.feature_account_impl.presentation.common.accountSource.SourceTypeChooserBottomSheetDialog
import jp.co.soramitsu.feature_account_impl.presentation.exporting.ExportSource
import kotlinx.android.synthetic.main.fragment_account_details.*
import org.greenrobot.eventbus.EventBus

private const val ACCOUNT_ADDRESS_KEY = "ACCOUNT_ADDRESS_KEY"

class AccountDetailsFragment : BaseFragment<AccountDetailsViewModel>() {

    companion object {
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
    ) = layoutInflater.inflate(R.layout.fragment_account_details, container, false)

    override fun initViews() {
        fearlessToolbar.setHomeButtonListener {
            viewModel.backClicked()
        }

        accountDetailsAddressView.setWholeClickListener {
            viewModel.addressClicked()
        }
        icon.setOnClickListener {
//            accountDetailsNameField.requestFocus()
//            accountDetailsNameField.content.requestFocus()
            showSoftInputWindow(accountDetailsNameField.content)

        }
        accountDetailsExport.setOnClickListener {
            viewModel.exportClicked()
        }
        sellAction.setOnClickListener {
            EventBus.getDefault().postSticky(EventBusMessageEvent("", EventEntity.EVENT_SAVE))
        }

        editPsw.setOnClickListener { EventBus.getDefault().postSticky(EventBusMessageEvent("", EventEntity.EVENT_PSW)) }
        exportPrivateKey.setOnClickListener { EventBus.getDefault().postSticky(EventBusMessageEvent("", EventEntity.EVENT_PRIVATE_KEY)) }
        exportKeyStore.setOnClickListener { EventBus.getDefault().postSticky(EventBusMessageEvent("", EventEntity.EVENT_KEY_STORE)) }
        backupMnemonic.setOnClickListener { EventBus.getDefault().postSticky(EventBusMessageEvent("", EventEntity.EVENT_BACKUP)) }
        protocal.setOnClickListener { EventBus.getDefault().postSticky(EventBusMessageEvent("", EventEntity.EVENT_PROTOCAL)) }
        accountDetailsNameField.content.filters = nameInputFilters()
        if (!TextUtils.isEmpty(Extras.headUrl))
            Glide.with(this).load(Extras.headUrl).into(mine_title_img)
    }

    override fun inject() {
        val address = arguments!![ACCOUNT_ADDRESS_KEY] as String

        FeatureUtils.getFeature<AccountFeatureComponent>(
                requireContext(),
                AccountFeatureApi::class.java
        )
                .accountDetailsComponentFactory()
                .create(this, address)
                .inject(this)
    }

    override fun subscribe(viewModel: AccountDetailsViewModel) {
        setupExternalActions(viewModel)
        viewModel.accountLiveData.observe { account ->
            accountDetailsAddressView.setMessage(account.address)
            accountDetailsAddressView.setTextIcon(account.image)

            accountDetailsEncryptionType.setMessage(account.cryptoTypeModel.name)
            if (TextUtils.isEmpty(account.name)) {
                accountDetailsNameField.content.setText("   ")
//                accountDetailsNameField.content.setText("")
            } else
                accountDetailsNameField.content.setText(account.name)

            accountDetailsNode.text = account.network.name
        }

        viewModel.networkModel.observe { networkModel ->
            accountDetailsNode.setCompoundDrawablesWithIntrinsicBounds(networkModel.networkTypeUI.icon, 0, 0, 0)
        }

        viewModel.showExportSourceChooser.observeEvent(::showExportSourceChooser)

        accountDetailsNameField.content.onTextChanged(viewModel::nameChanged)
    }

    private fun showExportSourceChooser(payload: Payload<ExportSource>) {
        SourceTypeChooserBottomSheetDialog(requireActivity(), payload, viewModel::exportTypeSelected)
                .show()
    }

    fun showSoftInputWindow(editText: EditText) {
        editText.setFocusable(true)
        editText.setFocusableInTouchMode(true)
        editText.requestFocus()
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }
}