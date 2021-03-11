package jp.co.soramitsu.app.root.presentation.tab

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import jp.co.soramitsu.common.di.scope.ScreenScope

@Subcomponent(
        modules = [
            MainTabModule::class
        ]
)
@ScreenScope
interface MainTabComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
                @BindsInstance fragment: Fragment
        ): MainTabComponent
    }

    fun inject(fragment: MainTabFragment)
}