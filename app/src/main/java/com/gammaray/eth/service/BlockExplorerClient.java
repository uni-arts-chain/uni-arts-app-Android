package com.gammaray.eth.service;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.gammaray.eth.base.C;
import com.gammaray.eth.domain.ETHWallet;
import com.gammaray.eth.entity.NetworkInfo;
import com.gammaray.eth.entity.Transaction;
import com.gammaray.eth.repository.EthereumNetworkRepository;
import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class BlockExplorerClient implements BlockExplorerClientType {

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final EthereumNetworkRepository networkRepository;

    private TransactionsApiClient transactionsApiClient;

    public BlockExplorerClient(
            OkHttpClient httpClient,
            Gson gson,
            EthereumNetworkRepository networkRepository) {
        this.httpClient = httpClient;
        this.gson = gson;
        this.networkRepository = networkRepository;
        this.networkRepository.addOnChangeDefaultNetwork(this::onNetworkChanged);
        NetworkInfo networkInfo = networkRepository.getDefaultNetwork();
        onNetworkChanged(networkInfo.rpcServerUrl);
    }

    private void buildApiClient(String baseUrl) {
        LogUtils.e("baseUrl ===" + baseUrl);
        transactionsApiClient = new Retrofit.Builder()
                .baseUrl(baseUrl + "/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TransactionsApiClient.class);
    }

    @Override
    public Observable<Transaction[]> fetchTransactions(ETHWallet address, String tokenAddr, String coin) {

        if (address != null) {
            coin = address.getSymble().equalsIgnoreCase("qts") ? C.QTS_NAME.toLowerCase() : C.ETH_NAME.toLowerCase();
            buildApiClient(C.TICKER_API_URL);
        }

        if (TextUtils.isEmpty(tokenAddr)) {
            return transactionsApiClient
                    .fetchTransactions(coin, address.getAddress())
                    .lift(apiError(gson))
                    .map(r -> r.docs)
                    .subscribeOn(Schedulers.io());
        } else {
            return transactionsApiClient
                    .fetchTransactions(coin, address.getAddress(), tokenAddr)
                    .lift(apiError(gson))
                    .map(r -> r.docs)
                    .subscribeOn(Schedulers.io());
        }

    }

    private void onNetworkChanged(String networkInfo) {
        buildApiClient(networkInfo);
    }

    private static @NonNull
    <T> ApiErrorOperator<T> apiError(Gson gson) {
        return new ApiErrorOperator<>(gson);
    }

    //	http://localhost:8000/transactions?address=0x856e604698f79cef417aab0c6d6e1967191dba43
    private interface TransactionsApiClient {
        @GET("/api/v1/{coin}/transactions/{address}")
        Observable<Response<EtherScanResponse>> fetchTransactions(
                @Path("coin") String coin, @Path("address") String address);

        @GET("api/v1/{coin}/transactions/{address}")
        Observable<Response<EtherScanResponse>> fetchTransactions(
                @Path("coin") String coincoin, @Path("address") String contract, @Query("token") String token);


    }

    private final static class EtherScanResponse {
        Transaction[] docs;
    }

    private final static class ApiErrorOperator<T> implements ObservableOperator<T, Response<T>> {

        private final Gson gson;

        public ApiErrorOperator(Gson gson) {
            this.gson = gson;
        }

        @Override
        public Observer<? super Response<T>> apply(Observer<? super T> observer) throws Exception {
            return new DisposableObserver<Response<T>>() {
                @Override
                public void onNext(Response<T> response) {
                    observer.onNext(response.body());
                    observer.onComplete();
                }

                @Override
                public void onError(Throwable e) {
                    observer.onError(e);
                }

                @Override
                public void onComplete() {
                    observer.onComplete();
                }
            };
        }
    }
}
