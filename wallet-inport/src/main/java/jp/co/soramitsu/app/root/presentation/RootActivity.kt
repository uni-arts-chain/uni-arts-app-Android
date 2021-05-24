package jp.co.soramitsu.app.root.presentation

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.upbest.arouter.ArouterModelPath
import com.upbest.arouter.EventBusMessageEvent
import com.upbest.arouter.EventEntity
import jp.co.soramitsu.app.root.di.RootApi
import jp.co.soramitsu.app.root.di.RootComponent
import jp.co.soramitsu.app.root.navigation.Navigator
import jp.co.soramitsu.common.base.BaseActivity
import jp.co.soramitsu.common.di.FeatureUtils
import jp.co.soramitsu.common.utils.EventObserver
import jp.co.soramitsu.common.utils.setVisible
import jp.co.soramitsu.common.utils.showToast
import jp.co.soramitsu.common.utils.updatePadding
import jp.co.soramitsu.inport.R
import jp.co.soramitsu.splash.presentation.SplashBackgroundHolder
import kotlinx.android.synthetic.main.activity_root.rootNetworkBar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@Route(path = ArouterModelPath.ROOT_ACTIVITY)
class RootActivity : BaseActivity<RootViewModel>(), SplashBackgroundHolder {

    @Inject
    lateinit var navigator: Navigator

    override fun inject() {
        FeatureUtils.getFeature<RootComponent>(this, RootApi::class.java)
                .mainActivityComponentFactory()
                .create(this)
                .inject(this)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        removeSplashBackground()

        viewModel.restoredAfterConfigChange()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        navigator.attach(navController, this)

        rootNetworkBar.setOnApplyWindowInsetsListener { view, insets ->
            view.updatePadding(top = insets.systemWindowInsetTop)

            insets
        }

        intent?.let(::processIntent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: EventBusMessageEvent) {
        if (event.getmMessage().equals(EventEntity.EVENT_FINISH))
            finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        navigator.detach()
    }

    override fun layoutResource(): Int {

        return R.layout.activity_root
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        processIntent(intent)
    }

    override fun initViews() {
    }

    override fun onStop() {
        super.onStop()

        viewModel.noticeInBackground()
    }

    override fun onStart() {
        super.onStart()

        viewModel.noticeInForeground()
    }

    override fun subscribe(viewModel: RootViewModel) {
        viewModel.showConnectingBarLiveData.observe(this, Observer { show ->
            rootNetworkBar.setVisible(show)
        })

        viewModel.messageLiveData.observe(this, EventObserver {
            showToast(it)
        })
    }

    override fun removeSplashBackground() {
    }

    override fun changeLanguage() {
        viewModel.noticeLanguageLanguage()

        recreate()

    }

    private fun processIntent(intent: Intent) {
        val uri = intent.data?.toString()

        uri?.let { viewModel.externalUrlOpened(uri) }
    }


    private val navController: NavController by lazy {
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment

        navHostFragment.navController
    }
}