package com.gammaray.eth.service;

import com.gammaray.eth.entity.AddTokenBean;
import com.gammaray.eth.entity.LoginBean;
import com.gammaray.eth.entity.MsgCode;
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
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.gammaray.eth.base.C.TICKER_API_URL_OTC;

/**
 * Created by Tiny 熊 @ Upchain.pro
 * WeiXin: xlbxiong
 */


public class WalletRequestService implements RequestService {

    //
    private final OkHttpClient httpClient;
    private final Gson gson;
    private ApiClient apiClient;

    public WalletRequestService(
            OkHttpClient httpClient,
            Gson gson) {
        this.httpClient = httpClient;
        this.gson = gson;
        buildApiClient(TICKER_API_URL_OTC);
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
    public Observable<MsgCode> getCode(String type, String action, String value) {
        return apiClient.getCode(type, action, value)
                .lift(apiError())
                .map(r -> r)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<LoginBean> login(String loginby, String code) {
        return apiClient.login(loginby, code)
                .lift(apiError())
                .map(r -> r)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<LoginBean> bind(String header, String type, String code) {
        return apiClient.bind(header, type, code)
                .lift(apiError())
                .map(r -> r)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<LoginBean> getMemberInfo(String header) {
        return apiClient.getMemberInfo(header)
                .lift(apiError())
                .map(r -> r)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<LoginBean> logOut(String header) {
        return apiClient.logout(header)
                .lift(apiError())
                .map(r -> r)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<LoginBean> upLoadImg(String header, RequestBody requestBody) {
        return apiClient.uploadImage(header, requestBody)
                .lift(apiError())
                .map(r -> r)
                .subscribeOn(Schedulers.io());
    }


    private static @NonNull
    <T> ApiErrorOperator<T> apiError() {
        return new ApiErrorOperator<>();
    }

    public interface ApiClient {

        @POST("/api/v1/configs/get_code")
        @FormUrlEncoded
        Observable<MsgCode> getCode(@Field("verify_type") String type, @Field("verify_action") String action, @Field("verify_value") String value);


        @POST("/api/v1/members/login")
        @FormUrlEncoded
        Observable<LoginBean> login(@Field("login_by") String type, @Field("code") String value);

        @POST("/api/v1/members/bind")
        @FormUrlEncoded
        Observable<LoginBean> bind(@Header("Authorization") String header, @Field("value") String value, @Field("code") String code);


        @POST("/api/v1/members/logout")
        Observable<LoginBean> logout(@Header("Authorization") String header);


        @GET("/api/v1/members/info")
        Observable<LoginBean> getMemberInfo(@Header("Authorization") String header);

        @POST("/api/v1/members/avatar")
        Observable<LoginBean> uploadImage(@Header("Authorization") String header, @Body RequestBody mRequestBody);

        @GET("/api/v1/assets")
        @Multipart
        Observable<Response<List<AddTokenBean>>> requestTokens(@Query("kw") String keyword, @Query("type") String type);

    }

    private static class TickerResponse {
        Ticker response;
//        List<Ticker> response;
    }

    private final static class ApiErrorOperator<T> implements ObservableOperator<T, T> {

        @Override
        public Observer<? super T> apply(Observer<? super T> observer) throws Exception {
            return new DisposableObserver<T>() {
                @Override
                public void onNext(T response) {
                    observer.onNext(response);
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
