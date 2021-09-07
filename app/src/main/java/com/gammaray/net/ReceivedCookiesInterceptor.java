package com.gammaray.net;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by gengda on 2017/11/22.
 */

public class ReceivedCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
//            HashSet<String> cookies = new HashSet<>();
//
//            for (String header : originalResponse.headers("Set-Cookie")) {
//                cookies.add(header);
//            }
            String[] cookies = originalResponse.header("Set-Cookie").split(";");
//            if (null != cookies && cookies.length > 0)
//                RxBus.getInstance().post(RxBusConfig.SET_COOKIE, cookies[0]);
        }

        return originalResponse;
    }
}
