package com.yunhualian.net;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.User;
import com.yunhualian.utils.DateUtil;
import com.yunhualian.utils.MD5Encrypt;
import com.yunhualian.utils.ParamsBuilder;
import com.yunhualian.utils.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.http.FormUrlEncoded;

public class RemoteParametersManager {
    private static String mLocalLanguage = "";
    private static String mLocalCountry = "";
    private static volatile RemoteParametersManager mRemoteParametersManager;

    protected HashMap<String, String> mParamsWithToken = new HashMap<>();

    public static RemoteParametersManager getInstance() {
        if (null == mRemoteParametersManager) {
            synchronized (RemoteParametersManager.class) {
                if (null == mRemoteParametersManager) {
                    mRemoteParametersManager = new RemoteParametersManager();
                }
            }
        }
        return mRemoteParametersManager;
    }

    public static String getmLocalLanguage() {
        if (TextUtils.isEmpty(mLocalLanguage))
            mLocalLanguage = Locale.getDefault().getLanguage() + "-" + getmLocalCountry();
        return mLocalLanguage;
    }

    public static String getmLocalCountry() {
        if (TextUtils.isEmpty(mLocalCountry))
            mLocalCountry = Locale.getDefault().getCountry();
        return mLocalCountry;
    }


    public String disposeParameters(Gson mGson, HashMap<String, Object> mContentMap) {
        /*String reqTime = TimeUtils.millis2String(System.currentTimeMillis(), AppConstant.getDefaultFormat());
        mContentMap.put("reqTime", reqTime);*/
        return null != mContentMap ? mGson.toJson(mContentMap) : null;
    }

    public FormBody disposeParameters(FormBody mFormBody) {
        FormBody.Builder nFormBody = new FormBody.Builder();
        for (int i = 0; i < mFormBody.size(); i++) {
            nFormBody.add(mFormBody.name(i), mFormBody.value(i));
        }
        String mToken;
        if (!TextUtils.isEmpty(mToken = YunApplication.getmUserVo(false).getToken()))
            nFormBody.add("token", mToken);
        return nFormBody.build();
    }

    public void disposeHeader(Request.Builder mRequestBuilder, Request request) throws IOException {
//        mRequestBuilder.header("Accept-Language", getmLocalLanguage());
        String mToken;
        mParamsWithToken.clear();
        if (!TextUtils.isEmpty(mToken = YunApplication.getToken())) {
            mRequestBuilder.header("Authorization", mToken);
            String currentTime = DateUtil.getCurrentSecondTime();
            if (request.method().equalsIgnoreCase("post")) {
                if (request.body() instanceof FormBody) {
                    FormBody formBody = (FormBody) request.body();
                    if (formBody.size() > 0)
                        for (int i = 0; i < formBody.size(); i++) {
                            String value = formBody.encodedValue(i);
                            String enc = "utf-8";
                            String newValue = URLDecoder.decode(value, enc);
                            mParamsWithToken.put(formBody.encodedName(i), newValue);
                        }
                } else if (request.body() instanceof MultipartBody) {

                    MultipartBody body = (MultipartBody) request.body();
                    Map<String, String> params = new HashMap<>();
                    Map<String, String> files = new HashMap<>();
                    for (MultipartBody.Part part : body.parts()) {
                        LogUtils.e("body parts===================");
                        RequestBody body1 = part.body();
                        Headers headers = part.headers();
                        if (headers != null && headers.size() > 0) {
                            LogUtils.e("split===================");
                            String[] split = headers.value(0).replace(" ", "").replace("\"", "").split(";");
                            if (split.length == 2) {
                                LogUtils.e("文本===================" + split.toString());
                                //文本
                                String[] keys = split[1].split("=");
                                if (keys.length > 1 && body1.contentLength() < 1024) {
                                    String key = keys[1];
                                    String value = "";
                                    Buffer buffer = new Buffer();
                                    body1.writeTo(buffer);
                                    value = buffer.readUtf8();
                                    params.put(key, value);
                                    mParamsWithToken.put(key, value);
                                    LogUtils.e("key===================" + key);
                                    LogUtils.e("value===================" + value);
                                }
                            } else if (split.length == 3) {
                                LogUtils.e("文件===================");
                                //文件
                                String fileKey = "";
                                String fileName = "";
                                String[] keys = split[1].split("=");
                                String[] names = split[2].split("=");
                                if (keys.length > 1) fileKey = keys[1];
                                if (names.length > 1) fileName = names[1];
                                files.put(fileKey, fileName);
                                mParamsWithToken.put(fileKey, fileName);
                            }
                        }

                    }
//                    System.out.println("文本参数-->" + params);
//                    System.out.println("文件参数-->" + files);

//                    MultipartBody multipartBody = (MultipartBody) request.body();
//                    multipartBody.part(0).headers().get("name");
//                    if (multipartBody.parts().size() > 1) {
//                        Map<String, List<String>> header = new HashMap<>();
//                        for (int i = 0; i < multipartBody.parts().size(); i++) {
//                            MultipartBody.Part multipart = multipartBody.part(i);
//
//                            Set<String> names = multipart.headers().names();
//                            LogUtils.e(names.toString());
//                            List<String> value = multipart.headers().values(names.toString());
//                            header = multipart.headers().toMultimap();
//                            List<String> values = header.get("content-disposition");
//                            LogUtils.e(values.get(0));
//                            if (i == 1) {
//                                String[] parms = values.get(0).split(";");
//
//                            }
//                        }
//                    }
                }
            } else {
                HttpUrl.Builder urlBuilder = request.url().newBuilder();
                HttpUrl httpUrl = urlBuilder.build();

                // 打印所有get参数
                Set<String> paramKeys = httpUrl.queryParameterNames();
                for (String key : paramKeys) {
                    String value = httpUrl.queryParameter(key);
                    Log.d("TEST", key + " " + value);
                    mParamsWithToken.put(key, value);
                }
            }

            mParamsWithToken.put(StringUtils.formatLowerCase("Tonce"), currentTime);
            mParamsWithToken = MD5Encrypt.orderByASC(mParamsWithToken);
            String signStr = ParamsBuilder.getSign(request, mParamsWithToken);
            LogUtils.e("signStr" + signStr);
            String sha256str = MD5Encrypt.HMACSHA256(signStr, YunApplication.getmUserVo().getExpire_at());
            LogUtils.e("signStr");
            mRequestBuilder.header("Sign",
                    sha256str);
            mRequestBuilder.header("Tonce", currentTime);
        }
    }
}
