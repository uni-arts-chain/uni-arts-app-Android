package jp.co.soramitsu.feature_account_impl.presentation.profile

import android.widget.Toast
import androidx.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.co.soramitsu.common.account.AddressIconGenerator
import jp.co.soramitsu.common.account.AddressModel
import jp.co.soramitsu.common.account.external.actions.ExternalAccountActions
import jp.co.soramitsu.common.base.BaseViewModel
import jp.co.soramitsu.common.resources.ResourceManager
import jp.co.soramitsu.common.utils.map
import jp.co.soramitsu.fearless_utils.extensions.toHexString
import jp.co.soramitsu.feature_account_api.domain.interfaces.AccountInteractor
import jp.co.soramitsu.feature_account_api.domain.model.Account
import jp.co.soramitsu.feature_account_api.domain.model.WithSeed
import jp.co.soramitsu.feature_account_impl.R
import jp.co.soramitsu.feature_account_impl.presentation.AccountRouter
import jp.co.soramitsu.feature_account_impl.presentation.exporting.ExportSource
import jp.co.soramitsu.feature_account_impl.presentation.exporting.ExportViewModel
import jp.co.soramitsu.feature_account_impl.presentation.language.mapper.mapLanguageToLanguageModel
import jp.co.soramitsu.feature_account_impl.presentation.language.model.LanguageModel

private const val AVATAR_SIZE_DP = 32

class EditWalletViewModel(
        private val interactor: AccountInteractor,
        private val router: AccountRouter,
        private val addressIconGenerator: AddressIconGenerator,
        private val externalAccountActions: ExternalAccountActions.Presentation,
        accountAddress: String,
        resourceManager: ResourceManager,
) : ExportViewModel(interactor, accountAddress, resourceManager, ExportSource.Seed), ExternalAccountActions by externalAccountActions {

    private val selectedAccountObservable = interactor.observeSelectedAccount()
    val seedLiveData = securityTypeLiveData.map {
        (it as WithSeed).seed!!.toHexString(withPrefix = true)
    }
    val selectedAccountLiveData: LiveData<Account> = selectedAccountObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asLiveData()

    val accountIconLiveData: LiveData<AddressModel> =
            observeIcon(selectedAccountObservable).asMutableLiveData()

    val selectedLanguageLiveData: LiveData<LanguageModel> =
            getSelectedLanguage().asMutableLiveData()

    fun aboutClicked() {
        router.openAboutScreen()
    }

    fun accountsClicked() {

    }

    fun networksClicked() {
//        router.openNodes()
        val account = selectedAccountLiveData.value ?: return

        val des = router.openExportJsonPassword(account.address)
        router.withPinCodeCheckRequired(des, pinCodeTitleRes = R.string.pricate_key_export)
    }

    fun languagesClicked() {
        val account = selectedAccountLiveData.value ?: return

        val des = router.openExportJsonPassword(account.address)
        router.withPinCodeCheckRequired(des, pinCodeTitleRes = R.string.account_export)
    }

    fun changePinCodeClicked() {
        router.openChangePinCode()
    }

    fun accountActionsClicked() {
        val account = selectedAccountLiveData.value ?: return

        externalAccountActions.showExternalActions(ExternalAccountActions.Payload(account.address, account.network.type))
    }

    private fun observeIcon(accountObservable: Observable<Account>): Observable<AddressModel> {
        return accountObservable
                .subscribeOn(Schedulers.io())
                .flatMapSingle { account ->
                    addressIconGenerator.createAddressModel(account.address, AVATAR_SIZE_DP, account.name.toString())
                }
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getSelectedLanguage(): Single<LanguageModel> {
        return interactor.getSelectedLanguage()
                .map(::mapLanguageToLanguageModel)
    }
}