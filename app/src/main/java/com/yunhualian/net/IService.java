package com.yunhualian.net;


import android.util.Log;


import com.yunhualian.utils.DateUtil;
import com.yunhualian.utils.MD5Encrypt;
import com.yunhualian.utils.ParamsBuilder;
import com.yunhualian.utils.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;

/**
 * Created by Yanmin on 2016/3/17.
 */
public abstract class IService<T> {
    public static HashMap<String, IService> mInstances = new HashMap<>();
    protected HashMap<String, String> mHeadMap = new HashMap<>();
    protected HashMap<String, String> mParamsWithToken = new HashMap<>();

    protected final String mUrl;
    protected final boolean mbResponseIsJson;
    protected T mApi;
    protected OkHttpClient httpClient;
    protected Request mRequest;

    /**
     * 构造函数
     *
     * @param url            请求的根地址
     * @param responseIsJson 返回的数据结构是否是Json结构
     */
    public IService(String url, boolean responseIsJson) {
        mUrl = url;
        mbResponseIsJson = responseIsJson;
        init();
    }

    /**
     * 单例模式创建对象，子类需重新该方法，传入子类class对象
     *
     * @param subClass 子类的class对象，子类必须集成IService
     * @return 子类对象
     */
    public static IService getInstance(Class<? extends IService> subClass) {
        String subClassName = subClass.getName();
        IService instance = mInstances.get(subClassName);

        if (instance == null) {
            try {
                // 子类构造函数必须设为public
                instance = (IService) Class.forName(subClass.getName()).newInstance();
                mInstances.put(subClassName, instance);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return instance;
    }

    /**
     * Retrofit Api（T）对象初始化
     *
     * @return
     */
    private void init() {
        if (mApi == null) {
            httpClient = Connection.getUnsafeOkHttpClient(addHeader());

            if (httpClient != null) {
                Retrofit.Builder builder = new Retrofit.Builder();
                builder.client(httpClient);
                builder.baseUrl(mUrl);
                if (mbResponseIsJson) {
                    builder.addConverterFactory(CustomerConverterFactory.create());
                }
                Retrofit retrofit = builder.build();
                Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

                mApi = (T) retrofit.create(tClass);
            }
        }
    }

    /**
     * Optional 如果需要在Request中添加Header，需重写该方法
     *
     * @return Interceptor
     */
    protected Interceptor addHeader() {
        return null;
    }


    protected void addHeaderWithSigh(String token, HashMap<String, String> params, String expire_at) {
        if (null == token || null == params || null == mHeadMap || null == expire_at || null == mRequest)
            return;

        Log.e("Request url path = ", mRequest.url().encodedPath());

        // 设置签名params
        mParamsWithToken.clear();
        mParamsWithToken = (HashMap<String, String>) params.clone();
//        mParamsWithToken.put(CommonConstants.HttpHeadConfig.TOKEN, token);
        mParamsWithToken.put(StringUtils.formatLowerCase(CommonConstants.HttpHeadConfig.TONCE), DateUtil.getCurrentSecondTime());


        // 设置head
        mHeadMap.clear();
        mParamsWithToken = MD5Encrypt.orderByASC(mParamsWithToken);
        mHeadMap.put(CommonConstants.HttpHeadConfig.AUTHORIZATION, token);
        mHeadMap.put(CommonConstants.HttpHeadConfig.SIGN, MD5Encrypt.HMACSHA256(ParamsBuilder.getSign(mRequest, mParamsWithToken), expire_at));

    }


}
