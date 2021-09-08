package com.gammaray.eth.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.LogUtils;
import com.gammaray.base.YunApplication;
import com.gammaray.eth.base.BaseViewModel;
import com.gammaray.eth.domain.ETHWallet;
import com.gammaray.eth.entity.AddTokenBean;
import com.gammaray.eth.entity.ErrorEnvelope;
import com.gammaray.eth.entity.LoginBean;
import com.gammaray.eth.entity.MsgCode;
import com.gammaray.eth.entity.NetworkInfo;
import com.gammaray.eth.entity.RegiseResponse;
import com.gammaray.eth.entity.RegistPushBean;
import com.gammaray.eth.entity.Ticker;
import com.gammaray.eth.entity.Token;
import com.gammaray.eth.interact.FetchTokensInteract;
import com.gammaray.eth.interact.FetchWalletInteract;
import com.gammaray.eth.repository.EthereumNetworkRepository;
import com.gammaray.eth.service.RequestService;
import com.gammaray.eth.service.TickerService;
import com.gammaray.eth.service.UpWalletTickerService;
import com.gammaray.eth.service.WalletRequestService;
import com.gammaray.eth.util.WalletDaoUtils;
import com.google.gson.Gson;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class TokensViewModel extends BaseViewModel {
    private final MutableLiveData<NetworkInfo> defaultNetwork = new MutableLiveData<>();

    private final MutableLiveData<ETHWallet> defaultWallet = new MutableLiveData<>();


    private final MutableLiveData<List<Ticker.TickersBean>> prices = new MutableLiveData<>();
    private final MutableLiveData<Token[]> tokens = new MutableLiveData<>();
    private final MutableLiveData<List<AddTokenBean>> addTokens = new MutableLiveData<>();
    private final MutableLiveData<String> code = new MutableLiveData<>();
    private final MutableLiveData<LoginBean> loginInfo = new MutableLiveData<>();
    private final MutableLiveData<BigDecimal> blockNum = new MutableLiveData<>();
    private final MutableLiveData<String> logout = new MutableLiveData<>();
    private final MutableLiveData<RegiseResponse> registResult = new MutableLiveData<>();
    public final EthereumNetworkRepository ethereumNetworkRepository;
    private final FetchWalletInteract findDefaultWalletInteract;

    private final FetchTokensInteract fetchTokensInteract;
    private final TickerService tickerService;
    private final RequestService requestServicel;

    TokensViewModel(
            EthereumNetworkRepository ethereumNetworkRepository,
            FetchWalletInteract findDefaultWalletInteract,
            FetchTokensInteract fetchTokensInteract) {
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.ethereumNetworkRepository = ethereumNetworkRepository;
        this.fetchTokensInteract = fetchTokensInteract;
        requestServicel = new WalletRequestService(YunApplication.okHttpClient(), new Gson());
        tickerService = new UpWalletTickerService(YunApplication.okHttpClient(), new Gson());
    }

    public void prepare() {
        progress.postValue(true);

        defaultNetwork.postValue(ethereumNetworkRepository.getDefaultNetwork());
        disposable = findDefaultWalletInteract.findDefault()
                .subscribe(this::onDefaultWallet, this::onError);

    }

    public void getBlockNum() throws ExecutionException, InterruptedException {

        blockNum.postValue(fetchTokensInteract.getBlockNum());
    }

    public void onBlockNum(BigDecimal bigDecimal) {
        blockNum.postValue(bigDecimal);
    }

    public void updateDefaultWallet(final long walletId) {

        Single.fromCallable(() -> {
            return WalletDaoUtils.updateCurrent(walletId);
        }).subscribe(this::onDefaultWallet);

    }

    private void onDefaultWallet(ETHWallet wallet) {
        defaultWallet.setValue(wallet);
        LogUtils.e("view model onDefaultWallet", "onDefaultWallet");
        fetchTokens();
    }

    public LiveData<NetworkInfo> defaultNetwork() {
        return defaultNetwork;
    }

    public LiveData<ETHWallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<Token[]> tokens() {
        return tokens;
    }

    public LiveData<String> codes() {
        return code;
    }

    public LiveData<String> logOutStr() {
        return logout;
    }

    public LiveData<ErrorEnvelope> error() {
        return error;
    }

    public LiveData<LoginBean> getLoginInfo() {
        return loginInfo;
    }

    public LiveData<List<AddTokenBean>> addTokens() {
        return addTokens;
    }

    public MutableLiveData<List<Ticker.TickersBean>> prices() {
        return prices;
    }

    public MutableLiveData<RegiseResponse> registerResult() {
        return registResult;
    }

    public MutableLiveData<BigDecimal> blockNum() {
        return blockNum;
    }

    public void fetchTokens() {
        progress.postValue(true);
        LogUtils.e("view model fetchTokens", "fetchTokens");
        disposable = fetchTokensInteract
                .fetch(defaultWallet.getValue().address)
                .subscribe(this::onTokens, this::onError);
    }

    private void onTokens(Token[] tokens) {
        LogUtils.e("view model Tokens", "onTokens");
        progress.postValue(false);
        this.tokens.postValue(tokens);
        LogUtils.e("times", System.currentTimeMillis());
        //  TODO： 是否出现重复调用
        getTicker("token.tokenInfo.symbol").subscribe(this::onPrice, this::onError);
//        for (Token token : tokens) {
//            if (token.balance != null) {
//
//            }
//        }

    }

    private void register(RegiseResponse result) {
        registResult.postValue(result);
    }

    public void getAddTokens(String keywords, String type) {
        getTokens(keywords, type).subscribe(this::onAddTokens, this::onError);
    }

    public void sendCodes(String type, String action, String value) {
        getCode(type, action, value).subscribe(this::onCode, this::onError);
    }

    public void getMemberInfo(String token) {
        requestMemberInfo(token).subscribe(this::onMemberInfo, this::onError);
    }

    public void logOut() {
        logOut(YunApplication.getToken()).subscribe(this::logOutSuccess, this::onError);
    }

    public void logOutSuccess(LoginBean loginBean) {
        if (loginBean != null) {
            if (loginBean.getHead().getCode().equals("1000")) {
                logout.postValue("suc");
            }
        }
    }


    public void upLoadImage(String path) {
        File file = new File(path);
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

        RequestBody requestFrontFile = RequestBody.create(MediaType.parse("image/*"), file);
        requestBody.addFormDataPart("file", file.getName(), requestFrontFile);
        uploadFile(YunApplication.getToken(), requestBody.build()).subscribe(this::LoginSuccess, this::onError);
    }

    public void onMemberInfo(LoginBean loginBean) {
        if (loginBean != null) {
            loginInfo.postValue(loginBean);
        }
    }

    public void loginAction(String loginBy, String code) {
        login(loginBy, code).subscribe(this::LoginSuccess, this::onError);
    }

    public void bindAction(String header, String type, String code) {
        bind(header, type, code).subscribe(this::bindSuccess, this::onError);
    }

    private void requestErro(Throwable throwable) {

    }

    public void onAddTokens(List<AddTokenBean> addTokenBeans) {
        this.addTokens.postValue(addTokenBeans);
    }

    public void onCode(MsgCode msgCode) {
        if (msgCode != null)
            if (msgCode.getHead().getCode().equals("1000"))
                this.code.postValue(msgCode.getBody().getCode());
            else onRequestError(msgCode.getHead().getMsg());
    }

    public void LoginSuccess(LoginBean loginBean) {
        if (loginBean != null) {
            loginInfo.postValue(loginBean);
        }
    }

    public void bindSuccess(LoginBean loginBean) {
        if (loginBean != null) {
            loginInfo.postValue(loginBean);
        }
    }

    public void onRequestError(String msg) {
        this.requestError.postValue(msg);
    }

    public Single<List<Ticker.TickersBean>> getTicker(String symbol) {
        return Single.fromObservable(tickerService
                .fetchTickerPrice(symbol, getCurrency()));   // getDefaultNetwork().symbol
    }

//    public void registerPushQ(RegisterPush_Q wallet) {
//        registerActionQ(wallet).subscribe(this::register, this::onError);
//    }
//
//    public void registerPushE(RegisterPush_E wallet) {
//        registerActionE(wallet).subscribe(this::register, this::onError);
//    }

    public void registerPush(RegistPushBean wallet) {
        registerAction(wallet).subscribe(this::register, this::onError);
    }

    public Single<List<AddTokenBean>> getTokens(String keywords, String type) {
        return Single.fromObservable(tickerService.getTokens(keywords, type));
    }

    public String getCurrency() {
        return ethereumNetworkRepository.getCurrency();
    }

    private void onPrice(List<Ticker.TickersBean> ticker) {
        this.prices.postValue(ticker);
    }

//    public Single<RegiseResponse> registerActionQ(RegisterPush_Q wallet) {
//        return Single.fromObservable(tickerService
//                .registerPushQ(wallet));   // getDefaultNetwork().symbol
//    }
//
//    public Single<RegiseResponse> registerActionE(RegisterPush_E wallet) {
//        return Single.fromObservable(tickerService
//                .registerPushE(wallet));   // getDefaultNetwork().symbol
//
//    }

    public Single<RegiseResponse> registerAction(RegistPushBean wallet) {
        return Single.fromObservable(tickerService
                .registerPush(wallet));   // getDefaultNetwork().symbol
    }

    public Single<MsgCode> getCode(String type, String action, String value) {
        return Single.fromObservable(requestServicel
                .getCode(type, action, value));   // getDefaultNetwork().symbol
    }

    public Single<LoginBean> login(String type, String value) {
        return Single.fromObservable(requestServicel
                .login(type, value));   // getDefaultNetwork().symbol
    }

    public Single<LoginBean> bind(String header, String type, String value) {
        return Single.fromObservable(requestServicel
                .bind(header, type, value));   // getDefaultNetwork().symbol
    }

    public Single<LoginBean> requestMemberInfo(String header) {
        return Single.fromObservable(requestServicel
                .getMemberInfo(header));   // getDefaultNetwork().symbol
    }

    public Single<LoginBean> uploadFile(String header, RequestBody requestBody) {
        return Single.fromObservable(requestServicel
                .upLoadImg(header, requestBody));   // getDefaultNetwork().symbol
    }


    public Single<LoginBean> logOut(String token) {
        return Single.fromObservable(requestServicel
                .logOut(token));   // getDefaultNetwork().symbol
    }

}


