package jp.co.soramitsu.feature_account_impl.presentation.profile.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import jp.co.soramitsu.common.di.scope.ScreenScope
import jp.co.soramitsu.feature_account_impl.presentation.profile.EditWalletFragment

@Subcomponent(
        modules = [
            EditModule::class
        ]
)
@ScreenScope
interface EditWalletComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
                @BindsInstance fragment: Fragment,
                @BindsInstance accountAddress: String
        ): EditWalletComponent
    }

    fun inject(editWalletFragment: EditWalletFragment)
}