package jp.co.soramitsu.feature_account_impl.presentation.mnemonic.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.upbest.arouter.EventBusMessageEvent
import com.upbest.arouter.EventEntity
import com.upbest.arouter.Extras
import jp.co.soramitsu.common.base.BaseFragment
import jp.co.soramitsu.common.di.FeatureUtils
import jp.co.soramitsu.common.utils.Event
import jp.co.soramitsu.common.utils.doOnGlobalLayout
import jp.co.soramitsu.common.utils.setVisible
import jp.co.soramitsu.feature_account_api.di.AccountFeatureApi
import jp.co.soramitsu.feature_account_impl.R
import jp.co.soramitsu.feature_account_impl.di.AccountFeatureComponent
import jp.co.soramitsu.feature_account_impl.presentation.mnemonic.confirm.view.MnemonicWordView
import kotlinx.android.synthetic.main.fragment_backup_mnemonic.toolbar
import kotlinx.android.synthetic.main.fragment_confirm_mnemonic.*
import org.greenrobot.eventbus.EventBus

class ConfirmMnemonicFragment : BaseFragment<ConfirmMnemonicViewModel>() {

    companion object {
        var address: String = ""
        private const val KEY_PAYLOAD = "confirm_payload"

        fun getBundle(payload: ConfirmMnemonicPayload): Bundle {

            return Bundle().apply {
                putParcelable(KEY_PAYLOAD, payload)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm_mnemonic, container, false)
    }

    override fun initViews() {
        toolbar.setHomeButtonListener {
            viewModel.homeButtonClicked()
        }

        toolbar.setRightActionClickListener {
            viewModel.resetConfirmationClicked()
        }

        confirmationMnemonicView.setOnClickListener {
            viewModel.removeLastWordFromConfirmation()
        }

        confirmationMnemonicView.disableWordDisappearAnimation()

        nextBtn.setOnClickListener {
            viewModel.nextButtonClicked()
        }

        conformMnemonicSkip.setOnClickListener {
            viewModel.skipClicked()
        }
        noData.setVisible(!Extras.isShow)
    }

    override fun inject() {
        val payload = argument<ConfirmMnemonicPayload>(KEY_PAYLOAD)

        FeatureUtils.getFeature<AccountFeatureComponent>(context!!, AccountFeatureApi::class.java)
                .confirmMnemonicComponentFactory()
                .create(this, payload)
                .inject(this)
    }

    override fun subscribe(viewModel: ConfirmMnemonicViewModel) {
        conformMnemonicSkip.setVisible(viewModel.skipVisible)

        wordsMnemonicView.doOnGlobalLayout {
            populateMnemonicContainer(viewModel.shuffledMnemonic)
        }

        viewModel.resetConfirmationEvent.observeEvent {
            confirmationMnemonicView.resetView()
            wordsMnemonicView.restoreAllWords()
        }

        viewModel.removeLastWordFromConfirmationEvent.observeEvent {
            confirmationMnemonicView.removeLastWord()
            wordsMnemonicView.restoreLastWord()
        }

        viewModel.nextButtonEnableLiveData.observe {
            nextBtn.isEnabled = it
        }

        viewModel.matchingMnemonicErrorAnimationEvent.observeEvent {
            textView2.setVisible(true)
            textView2.text = "助记词不正确"
            playMatchingMnemonicErrorAnimation()
        }
        if (!Extras.isShow)
            conformMnemonicSkip.performClick()
    }

    private fun populateMnemonicContainer(mnemonicWords: List<String>) {
        val words = mnemonicWords.map { mnemonicWord ->
            MnemonicWordView(activity!!).apply {
                setWord(mnemonicWord)
                setColorMode(MnemonicWordView.ColorMode.LIGHT)
                setOnClickListener { wordClickListener(this, mnemonicWord) }
                measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            }
        }
        wordsMnemonicView.populateWithMnemonic(words)

        val containerHeight = wordsMnemonicView.getMinimumMeasuredHeight()
        wordsMnemonicView.minimumHeight = containerHeight
        confirmationMnemonicView.minimumHeight = containerHeight
    }

    private val wordClickListener: (MnemonicWordView, String) -> Unit = { mnemonicWordView, word ->
        viewModel.addWordToConfirmMnemonic(word)

        wordsMnemonicView.removeWordView(mnemonicWordView)

        val wordView = MnemonicWordView(activity!!).apply {
            setWord(word)
            setColorMode(MnemonicWordView.ColorMode.DARK)
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        }
        confirmationMnemonicView.populateWord(wordView)
    }

    private fun playMatchingMnemonicErrorAnimation() {
        val animation = AnimationUtils.loadAnimation(activity!!, R.anim.shake)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                viewModel.matchingErrorAnimationCompleted()
            }
        })
        confirmationMnemonicView.startAnimation(animation)
    }
}