package com.yunhualian.net;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yunhualian.R;
import com.yunhualian.base.ActivityManager;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.YunApplication;
import com.yunhualian.constant.AppConstant;
import com.yunhualian.entity.BaseResponseVo;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.yunhualian.BuildConfig;

public class NetworkManager {
    private static final int HTTP_CONNECT_TIMEOUT = 30;
    private static final int HTTP_READ_TIMEOUT = 30;
    private static final int HTTP_WRITE_TIMEOUT = 30;

    private static final int HTTP_CONNECT_TIMEOUT_LONG = 30;
    private static final int HTTP_READ_TIMEOUT_LONG = 30;
    private static volatile NetworkManager mRequestManager;
    private static RemoteService mRemoteService;

    public static NetworkManager instance() {
        if (null == mRequestManager) {
            synchronized (NetworkManager.class) {
                if (null == mRequestManager) {
                    mRequestManager = new NetworkManager();
                }
            }
        }
        return mRequestManager;
    }

    public RemoteService init() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()

                .addInterceptor(new LoggingInterceptor())

                .addInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstant.ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return mRemoteService = retrofit.create(RemoteService.class);
    }

    /*
     * */
    public RemoteService getmRemoteService() {
        return null != mRemoteService ? mRemoteService : init();
    }

    private static class LoggingInterceptor implements Interceptor {
        Gson mGson;

        public LoggingInterceptor() {
            this.mGson = new GsonBuilder()
                    .registerTypeAdapter(
                            new TypeToken<HashMap<String, Object>>() {
                            }.getType(),
                            new JsonDeserializer<HashMap<String, Object>>() {
                                @Override
                                public HashMap<String, Object> deserialize(
                                        JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    JsonObject jsonObject = json.getAsJsonObject();
                                    Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                                    for (Map.Entry<String, JsonElement> entry : entrySet) {
                                        hashMap.put(entry.getKey(), entry.getValue());
                                    }
                                    return hashMap;
                                }
                            }).create();
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder requestBuilder = request.newBuilder();
            requestBuilder.addHeader("Accept-Language", "zh-CN");
            RemoteParametersManager.getInstance().disposeHeader(requestBuilder, request);

            if (request.method().equals("POST")) {
                RequestBody requestBody = request.body();
                if (requestBody instanceof RemoteRequestBody) {
                    String mContent = ((RemoteRequestBody) requestBody).getContent();
                    HashMap<String, Object> mContentMap = mGson.fromJson(mContent, new TypeToken<HashMap<String, Object>>() {
                    }.getType());

                    String mParametes;
                    requestBuilder.post(RequestBody.create(null != request.body() && null != request.body().contentType() ? request.body().contentType() :
                                    MediaType.parse("application/json; charset=utf-8"),
                            mParametes = request.url().toString().contains(AppConstant.ADDRESS) &&
                                    !TextUtils.isEmpty(mContent) ? RemoteParametersManager.getInstance().disposeParameters(mGson, mContentMap) : mContent));
                } else if (requestBody instanceof FormBody) {

//                    FormBody mBody = (FormBody) requestBody;
//                    requestBuilder.post(request.url().toString().contains(AppConstant.ADDRESS) ? RemoteParametersManager.getInstance().disposeParameters(mBody) : requestBody);
                }
            } else {
            }
            return chain.proceed(requestBuilder.build());
        }
    }

    private <T> boolean checkNetWork(MinerCallback<T> mCallBack, Call<T> mCall) {
        if (!NetworkUtils.isConnected()) {
            dismissLoading(mCallBack);
            if (null != mCallBack) {
                String mMsg = YunApplication.getInstance().getResources().getString(R.string.net_word_error);
                ToastUtils.showShort(mMsg);
                mCallBack.onFailure(mCall, new NetworkErrorException(mMsg));
            }
            return false;
        }
        return true;
    }

    private <T> boolean checkNull(JsonObject mRequestJson, MinerCallback<T> mCallBack) {
        if (null == mRequestJson) {
            if (null != mCallBack) {
                mCallBack.onFailure(null, new Throwable(YunApplication.getInstance().getResources().getString(R.string.parameter_error)));
            }
            return true;
        }
        return false;
    }

    public <T> void postReq(final MinerCallback<T> mCallBack, final Call<T> mCall) {
        if (checkNetWork(mCallBack, mCall)) {
            if (null == mCall) {
                if (null != mCallBack) {
                    String mMsg = YunApplication.getInstance().getResources().getString(R.string.request_error);
                    ToastUtils.showShort(mMsg);
                    mCallBack.onFailure(mCall, new RuntimeException(mMsg));
                }
                return;
            }
            mCall.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, retrofit2.Response<T> response) {
                    dismissLoading(mCallBack);
                    if (null == response || null == response.body()) {
                        if (null != mCallBack) {
                            BaseResponseVo mErrorResponse = null;
                            String mMsg = YunApplication.getInstance().getResources().getString(R.string.reponse_error);
                            if (null != response.errorBody()) {
                                try {
                                    mErrorResponse = GsonUtils.fromJson(response.errorBody().string(), BaseResponseVo.class);
                                    if (null != mErrorResponse && null != mErrorResponse.getHead() && mErrorResponse.getHead().getCode() == 1070) {
                                        if (YunApplication.isLogin())
                                            YunApplication.logout(false);
                                    }

                                    mMsg = null != mErrorResponse && null != mErrorResponse.getHead() ? mErrorResponse.getHead().getMsg() : mMsg;

                                } catch (IOException | IllegalStateException | JsonSyntaxException e) {
                                    e.printStackTrace();
                                }
                            }
                            ToastUtils.showShort(mMsg);
                            mCallBack.onFailure(mCall, new ErrorThrowable(mErrorResponse));
                        }
                        return;
                    }

                    if (response.body() instanceof BaseResponseVo) {
                        BaseResponseVo mBaseResponseVo = ((BaseResponseVo) response.body());
                        if (mBaseResponseVo.isSuccessful()) {
                            if (null != mCallBack) {
                                mCallBack.onSuccess(call, response);
                            }
                        } else {
                            if (null != mBaseResponseVo.getHead()) {
                                if (!TextUtils.isEmpty(mBaseResponseVo.getHead().getMsg())) {
                                    ToastUtils.showShort(mBaseResponseVo.getHead().getMsg());
                                }
                                if (null != mCallBack) {
                                    mCallBack.onError(call, response);
                                }
                            }
                        }
                    } else {
                        if (null != mCallBack) {
                            mCallBack.onSuccess(call, response);
                        }
                    }

                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    dismissLoading(mCallBack);
                    /*if (null != t && !TextUtils.isEmpty(t.getMessage())) {
                        ToastUtils.showShort(t.getMessage());
                    }*/
                    if (null != t) {
                        t.printStackTrace();
                    }
                    if (null != mCallBack) {
                        mCallBack.onFailure(mCall, t);
                    }

                }
            });
        }
    }

    private void dismissLoading(MinerCallback mCallBack) {
        String mName = mCallBack.getClass().toString();
        Activity mActivity = ActivityManager.getAppManager().findActivity(mName);


        if (mActivity instanceof BaseActivity)
            ((BaseActivity) mActivity).dismissLoading();

        mActivity = ActivityManager.getAppManager().currentActivity();

        if (mActivity instanceof BaseActivity)
            ((BaseActivity) mActivity).dismissLoading();
    }
}
