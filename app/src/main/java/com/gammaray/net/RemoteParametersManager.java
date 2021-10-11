package com.gammaray.net;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.gammaray.base.YunApplication;
import com.gammaray.utils.DateUtil;
import com.gammaray.utils.MD5Encrypt;
import com.gammaray.utils.ParamsBuilder;
import com.gammaray.utils.StringUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

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
        String mToken = "";
        if (YunApplication.getmUserVo() != null)
            mToken = YunApplication.getmUserVo().getToken();
        mParamsWithToken.clear();
        if (!TextUtils.isEmpty(mToken)) {
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
                        RequestBody body1 = part.body();
                        Headers headers = part.headers();
                        if (headers != null && headers.size() > 0) {
                            String[] split = headers.value(0).replace(" ", "").replace("\"", "").split(";");
                            if (split.length == 2) {
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
                                }
                            } else if (split.length == 3) {
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
                }
            } else {
                HttpUrl.Builder urlBuilder = request.url().newBuilder();
                HttpUrl httpUrl = urlBuilder.build();

                // 打印所有get参数
                Set<String> paramKeys = httpUrl.queryParameterNames();
                for (String key : paramKeys) {
                    String value = httpUrl.queryParameter(key);
                    mParamsWithToken.put(key, value);
                }
            }
            MD5Encrypt md5Encrypt = new MD5Encrypt();
            mParamsWithToken.put(StringUtils.formatLowerCase("Tonce"), currentTime);
            mParamsWithToken = md5Encrypt.orderByASC(mParamsWithToken);
            String signStr = "";
            try {
                signStr = ParamsBuilder.getSign(request, mParamsWithToken);
                LogUtils.e("signStr" + signStr);
            }catch (Exception e){
                e.printStackTrace();
            }
            String sha256str = null;
            if (YunApplication.getmUserVo() != null)
                if (!TextUtils.isEmpty(YunApplication.getmUserVo().getExpire_at())) {
                    sha256str = md5Encrypt.HMACSHA256(signStr, YunApplication.getmUserVo().getExpire_at());
                    mRequestBuilder.header("Sign",
                            sha256str);
                }
            mRequestBuilder.header("Tonce", currentTime);
        }
    }
}
