package jp.co.soramitsu.feature_account_impl.presentation.profile.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import jp.co.soramitsu.common.account.AddressIconGenerator
import jp.co.soramitsu.common.account.external.actions.ExternalAccountActions
import jp.co.soramitsu.common.di.viewmodel.ViewModelKey
import jp.co.soramitsu.common.di.viewmodel.ViewModelModule
import jp.co.soramitsu.common.resources.ResourceManager
import jp.co.soramitsu.feature_account_api.domain.interfaces.AccountInteractor
import jp.co.soramitsu.feature_account_impl.presentation.AccountRouter
import jp.co.soramitsu.feature_account_impl.presentation.profile.EditWalletViewModel
import jp.co.soramitsu.feature_account_impl.presentation.profile.ProfileViewModel

@Module(includes = [ViewModelModule::class])
class EditModule {

    @Provides
    @IntoMap
    @ViewModelKey(EditWalletViewModel::class)
    fun provideViewModel(
            interactor: AccountInteractor,
            router: AccountRouter,
            addressIconGenerator: AddressIconGenerator,
            externalAccountActions: ExternalAccountActions.Presentation,
            accountAddress: String,
            resourceManager: ResourceManager,
    ): ViewModel {
        return EditWalletViewModel(
                interactor,
                router,
                addressIconGenerator,
                externalAccountActions,
                accountAddress,
                resourceManager
        )
    }

    @Provides
    fun provideViewModelCreator(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): EditWalletViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(EditWalletViewModel::class.java)
    }
}