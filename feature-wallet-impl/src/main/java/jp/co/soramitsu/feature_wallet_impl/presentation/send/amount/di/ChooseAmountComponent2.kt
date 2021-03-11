package jp.co.soramitsu.feature_wallet_impl.presentation.send.amount.di

import androidx.fragment.app.FragmentActivity
import dagger.BindsInstance
import dagger.Subcomponent
import jp.co.soramitsu.common.di.scope.ScreenScope

@Subcomponent(
        modules = [
            ChooseAmountModule2::class
        ]
)
@ScreenScope
interface ChooseAmountComponent2 {

    @Subcomponent.Factory
    interface Factory {

        fun create(
                @BindsInstance activity: FragmentActivity,
                @BindsInstance address: String
        ): ChooseAmountComponent2


    }

    fun inject(activity: TestActivity)
}