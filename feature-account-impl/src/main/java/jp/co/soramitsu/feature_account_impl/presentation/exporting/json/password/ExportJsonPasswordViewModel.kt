package jp.co.soramitsu.feature_account_impl.presentation.exporting.json.password

import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.launcher.ARouter
import com.upbest.arouter.ArouterModelPath
import com.upbest.arouter.EventBusMessageEvent
import com.upbest.arouter.EventEntity
import com.upbest.arouter.Extras
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.co.soramitsu.common.base.BaseViewModel
import jp.co.soramitsu.common.utils.combine
import jp.co.soramitsu.common.utils.plusAssign
import jp.co.soramitsu.feature_account_api.domain.interfaces.AccountInteractor
import jp.co.soramitsu.feature_account_impl.presentation.AccountRouter
import jp.co.soramitsu.feature_account_impl.presentation.exporting.json.confirm.ExportJsonConfirmPayload
import org.greenrobot.eventbus.EventBus

class ExportJsonPasswordViewModel(
        private val router: AccountRouter,
        private val interactor: AccountInteractor,
        private val accountAddress: String
) : BaseViewModel() {
    val passwordLiveData = MutableLiveData<String>()
    val passwordConfirmationLiveData = MutableLiveData<String>()

    val showDoNotMatchingErrorLiveData = passwordLiveData.combine(passwordConfirmationLiveData) { password, confirmation ->
        confirmation.isNotBlank() && confirmation != password
    }

    val nextEnabled = passwordLiveData.combine(passwordConfirmationLiveData, initial = false) { password, confirmation ->
        password.isNotBlank() && confirmation.isNotBlank() && password == confirmation
    }

    fun back() {
        router.back()
    }

    fun nextClicked() {
//        val password = passwordLiveData.value!!
        val password = Extras.keyStorePsw
        disposables += interactor.generateRestoreJson(accountAddress, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { json ->
                    val payload = ExportJsonConfirmPayload(accountAddress, json)
//                    router.openExportJsonConfirm(payload)
//                    router.openExportKeyStore(json)
                    Extras.keyStore = json
                    Extras.exportAddress = accountAddress
                    Extras.exportJson = json
                    EventBus.getDefault().postSticky(EventBusMessageEvent(EventEntity.EVENT_EXPORT_JSON, payload))
                }
    }
}