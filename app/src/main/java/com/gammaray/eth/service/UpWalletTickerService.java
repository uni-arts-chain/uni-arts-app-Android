package com.gammaray.eth.service;

import com.gammaray.eth.entity.AddTokenBean;
import com.gammaray.eth.entity.MsgCode;
import com.gammaray.eth.entity.RegiseResponse;
import com.gammaray.eth.entity.RegistPushBean;
import com.gammaray.eth.entity.RegisterPush_E;
import com.gammaray.eth.entity.RegisterPush_Q;
import com.gammaray.eth.entity.Ticker;
import com.google.gson.Gson;

import java.util.List;

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
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.gammaray.eth.base.C.TICKER_API_URL;
import static com.gammaray.eth.base.C.TICKER_API_URL_OTC;

/**
 * Created by Tiny 熊 @ Upchain.pro
 * WeiXin: xlbxiong
 */


public class UpWalletTickerService implements TickerService {

    //
    private final OkHttpClient httpClient;
    private final Gson gson;
    private ApiClient apiClient;

    public UpWalletTickerService(
            OkHttpClient httpClient,
            Gson gson) {
        this.httpClient = httpClient;
        this.gson = gson;
        buildApiClient(TICKER_API_URL);
    }

    private void buildApiClient(String baseUrl) {
        apiClient = new Retrofit.Builder()
                .baseUrl(baseUrl + "/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ApiClient.class);
    }

    @Override
    public Observable<List<Ticker.TickersBean>> fetchTickerPrice(String symbols, String currency) {
        buildApiClient(TICKER_API_URL);
        return apiClient
                .fetchTickerPrice(currency)
                .lift(apiError())
                .map(r -> r.getTickers())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<RegiseResponse> registerPush(RegistPushBean wallet) {
        buildApiClient(TICKER_API_URL);
        return apiClient
                .registerPush(wallet)
                .lift(apiError())
                .map(r -> r)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<MsgCode> getCode(String type, String value) {
        buildApiClient(TICKER_API_URL_OTC);
        return apiClient.getCode(type, value)
                .lift(apiError())
                .map(r -> r)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<AddTokenBean>> getTokens(String keyword, String typs) {
        buildApiClient(TICKER_API_URL);
        return apiClient
                .requestTokens(keyword, typs)
                .lift(apiError())
                .map(r -> r)
                .subscribeOn(Schedulers.io());
    }

    private static @NonNull
    <T> ApiErrorOperator<T> apiError() {
        return new ApiErrorOperator<>();
    }

    public interface ApiClient {
        @GET("/api/v1/prices/")
        Observable<Response<Ticker>> fetchTickerPrice(@Query("currency") String currency);

        //        @POST("/api/v1/push/register")
//        @FormUrlEncoded
//        Observable<Response<String>> registerPush(@Field("deviceID") String devicedID,
//                                                  @Field("preferences") String perference,
//                                                  @Field("type") String type,
//                                                  @Field("token") String token,
//
//                                                  @Field("wallets") String walletRemoteBean);
        @POST("/api/v1/push/register")
        Observable<Response<RegiseResponse>> registerPushE(@Body RegisterPush_E body);

        @POST("/api/v1/push/register")
        Observable<Response<RegiseResponse>> registerPush(@Body RegistPushBean body);

        @POST("/api/v1/configs/get_code")
        @FormUrlEncoded
        Observable<Response<MsgCode>> getCode(@Field("verify_type") String type, @Field("verify_value") String value);

        @POST("/api/v1/push/register")
        Observable<Response<RegiseResponse>> registerPushQ(@Body RegisterPush_Q body);

        @GET("/api/v1/assets")
        Observable<Response<List<AddTokenBean>>> requestTokens(@Query("kw") String keyword, @Query("type") String type);

    }

    private static class TickerResponse {
        Ticker response;
//        List<Ticker> response;
    }

    private final static class ApiErrorOperator<T> implements ObservableOperator<T, Response<T>> {

        @Override
        public Observer<? super Response<T>> apply(Observer<? super T> observer) throws Exception {
            return new DisposableObserver<Response<T>>() {
                @Override
                public void onNext(Response<T> response) {
                    if (response.body() != null) {
                        observer.onNext(response.body());
                        observer.onComplete();
                    } else {
                        onError(new Throwable());
                    }
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
