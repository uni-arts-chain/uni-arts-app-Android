package jp.co.soramitsu.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins
import jp.co.soramitsu.app.di.app.AppComponent
import jp.co.soramitsu.app.di.app.DaggerAppComponent
import jp.co.soramitsu.app.di.deps.FeatureHolderManager
import jp.co.soramitsu.common.data.network.AndroidLogger
import jp.co.soramitsu.common.di.CommonApi
import jp.co.soramitsu.common.di.FeatureContainer
import jp.co.soramitsu.common.resources.ContextManager
import jp.co.soramitsu.common.resources.LanguagesHolder
import java.util.logging.Logger
import javax.inject.Inject
import kotlin.jvm.Throws
import kotlin.math.log

open class App : Application(), FeatureContainer {
    @Inject
    lateinit var featureHolderManager: FeatureHolderManager

    private lateinit var appComponent: AppComponent
    private val languagesHolder: LanguagesHolder = LanguagesHolder()
    private val logger = AndroidLogger()
    override fun attachBaseContext(base: Context) {
        val contextManager = ContextManager.getInstanceOrInit(base, languagesHolder)
        super.attachBaseContext(contextManager.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val contextManager = ContextManager.getInstanceOrInit(this, languagesHolder)
        contextManager.setLocale(this)
    }

    override fun onCreate() {
        super.onCreate()
        val contextManger = ContextManager.getInstanceOrInit(this, languagesHolder)

        appComponent = DaggerAppComponent
                .builder()
                .application(this)
                .contextManager(contextManger)
                .build()
        appComponent.inject(this)
        RxJavaPlugins.setErrorHandler(Consumer<Throwable?>() {
            @Throws(Exception::class)
            fun accept(throwable: Throwable) {
                throwable.printStackTrace() //这里处理所有的Rxjava异常
            }
        })
        ARouter.openDebug()
        ARouter.init(this)
    }

    override fun <T> getFeature(key: Class<*>): T {
        return featureHolderManager.getFeature<T>(key)!!
    }

    override fun releaseFeature(key: Class<*>) {
        featureHolderManager.releaseFeature(key)
    }

    override fun commonApi(): CommonApi {
        return appComponent
    }
}