package com.gammaray.eth.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gammaray.base.BaseActivity;
import com.gammaray.eth.base.BaseViewModel;
import com.gammaray.eth.base.C;
import com.gammaray.eth.domain.ETHWallet;
import com.gammaray.eth.entity.ConfirmationType;
import com.gammaray.eth.entity.GasSettings;
import com.gammaray.eth.entity.NetworkInfo;
import com.gammaray.eth.interact.CreateTransactionInteract;
import com.gammaray.eth.interact.FetchGasSettingsInteract;
import com.gammaray.eth.interact.FetchWalletInteract;
import com.gammaray.eth.repository.EthereumNetworkRepository;
import com.gammaray.eth.util.BalanceUtils;

import org.web3j.protocol.core.methods.request.Transaction;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.reactivex.schedulers.Schedulers;

public class ConfirmationViewModel extends BaseViewModel {
    private final MutableLiveData<String> newTransaction = new MutableLiveData<>();
    private final MutableLiveData<ETHWallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<GasSettings> gasSettings = new MutableLiveData<>();

    private GasSettings gasSettingsOverride = null;   // use setting

    private final MutableLiveData<NetworkInfo> defaultNetwork = new MutableLiveData<>();

    private final EthereumNetworkRepository ethereumNetworkRepository;
    private final FetchWalletInteract findDefaultWalletInteract;
    private final FetchGasSettingsInteract fetchGasSettingsInteract;
    private final CreateTransactionInteract createTransactionInteract;

    ConfirmationType confirmationType;


    private String mFromAddress;
    private String mToAddress;
    private BigInteger mValue;
    private BigInteger mCurGasPrice;
    private String mData;
    private String mGasLimit;

    public ConfirmationViewModel(
            EthereumNetworkRepository ethereumNetworkRepository,
            FetchWalletInteract findDefaultWalletInteract,
            FetchGasSettingsInteract fetchGasSettingsInteract,
            CreateTransactionInteract createTransactionInteract) {
        this.ethereumNetworkRepository = ethereumNetworkRepository;
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.fetchGasSettingsInteract = fetchGasSettingsInteract;
        this.createTransactionInteract = createTransactionInteract;
    }

    public LiveData<NetworkInfo> defaultNetwork() {
        return defaultNetwork;
    }

    public LiveData<ETHWallet> defaultWallet() {
        return defaultWallet;
    }

    public MutableLiveData<GasSettings> gasSettings() {
        return gasSettings;
    }

    public LiveData<String> sendTransaction() {
        return newTransaction;
    }

    public void overrideGasSettings(GasSettings settings) {
        gasSettingsOverride = settings;
        gasSettings.postValue(settings);
    }

    public void prepare(BaseActivity ctx, ConfirmationType type, String fromAddress, String toAddress, BigInteger value, String gasLimit, String data) {
        this.confirmationType = type;
        this.mFromAddress = fromAddress;
        this.mToAddress = toAddress;
        this.mValue = value;
        this.mGasLimit = gasLimit;
        this.mData = data;
        disposable = ethereumNetworkRepository
                .find()
                .subscribe(this::onDefaultNetwork, this::onError);

        fetchGasSettingsInteract.gasPriceUpdate().observe(ctx, this::onGasPrice);  // listen price
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        fetchGasSettingsInteract.clean();
    }


    private void onDefaultNetwork(NetworkInfo networkInfo) {
        defaultNetwork.postValue(networkInfo);
        disposable = findDefaultWalletInteract
                .findDefault()
                .subscribe(this::onDefaultWallet, this::onError);
    }

    private void onCreateTransaction(String transaction) {
        progress.postValue(false);
        newTransaction.postValue(transaction);
    }

    public void createTransaction(String password, String to, BigInteger amount, BigInteger gasPrice, BigInteger gasLimit) {
        progress.postValue(true);

        createTransactionInteract.createEthTransaction(defaultWallet.getValue(), to, amount, gasPrice, gasLimit, password)
                .subscribeOn(Schedulers.io())
                .subscribe(this::onCreateTransaction, this::onError);

    }

    public void createContractTransaction(String toAddress, BigInteger gasPrice, BigInteger gasLimit, BigInteger value, String data, String password) {
        if (defaultWallet.getValue() != null) {
            createTransactionInteract.createTransaction(defaultWallet.getValue(), toAddress, value, gasPrice, gasLimit, data, password).subscribeOn(Schedulers.io())
                    .subscribe(this::onCreateTransaction, this::onError);
        }
    }

    public void createTokenTransfer(String password, String to, String contractAddress,
                                    BigInteger amount, BigInteger gasPrice, BigInteger gasLimit) {
        progress.postValue(true);
        createTransactionInteract.createERC20Transfer(defaultWallet.getValue(), to, contractAddress, amount, gasPrice, gasLimit, password)
                .subscribeOn(Schedulers.io())
                .subscribe(this::onCreateTransaction, this::onError);
    }

    private void onDefaultWallet(ETHWallet wallet) {
        defaultWallet.setValue(wallet);
        if (gasSettings.getValue() == null) {
            fetchGasSettingsInteract.fetch(confirmationType, BalanceUtils.gweiToWei(new BigDecimal(mGasLimit))).subscribe(this::onGasSettings, this::onError);
        }
    }

    public void calculateGasSettings(byte[] transaction, boolean isNonFungible) {
        if (gasSettings.getValue() == null) {
            disposable = fetchGasSettingsInteract
                    .fetch(transaction, isNonFungible)
                    .subscribe(this::onGasSettings, this::onError);
        }
    }


    private void onGasSettings(GasSettings gasSettings) {
        this.gasSettings.setValue(gasSettings);
    }

    private void onGasPrice(BigInteger currentGasPrice) {
        if (this.gasSettings.getValue() != null //protect against race condition
                && this.gasSettingsOverride == null //only update if user hasn't overriden
        ) {
            Transaction transaction = new Transaction(mFromAddress, null, currentGasPrice, null, mToAddress, mValue, mData);
            fetchGasSettingsInteract.getTransactionGasLimit(transaction)
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::onGasLimit, this::onError);
            mCurGasPrice = currentGasPrice;
        }
    }

    private void onGasLimit(BigInteger gasLimit) {
        GasSettings updateSettings = new GasSettings(mCurGasPrice, gasLimit);
        this.gasSettings.postValue(updateSettings);
    }
}
