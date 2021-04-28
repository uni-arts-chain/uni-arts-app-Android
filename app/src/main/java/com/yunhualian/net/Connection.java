package com.yunhualian.net;

import android.os.Build;


import com.yunhualian.utils.DateUtil;
import com.yunhualian.utils.SharedPreUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

import com.yunhualian.BuildConfig;

/**
 * Created by zhangzhongqiang on 2015/7/29.
 * Refactored by Yanmin on 2016/3/16
 */
public class Connection {

    public static OkHttpClient getUnsafeOkHttpClient(Interceptor interceptor) {
        OkHttpClient okHttpClient = null;
        try {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();

            if (BuildConfig.DEBUG)
                logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                logInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.connectTimeout(20, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);
            builder.sslSocketFactory(new CustomerSSL(trustManagerForCertificates), trustManagerForCertificates);
            builder.addInterceptor(chain -> {
                Request request = chain.request();
                Request.Builder builder1 = request.newBuilder();
                builder1.addHeader(CommonConstants.HttpHeadConfig.TONCE, DateUtil.getCurrentSecondTime())
                        .addHeader(CommonConstants.HttpHeadConfig.PLATFORM, "android")
                        .addHeader(CommonConstants.HttpHeadConfig.LANGUAGE, judgeChinese() ? "zh-CN" : "en");
                Response response = chain.proceed(builder1.build());
                return response;
            });

            builder.interceptors().add(new ReceivedCookiesInterceptor());
            builder.interceptors().add(interceptor);
            builder.interceptors().add(logInterceptor);
            builder.retryOnConnectionFailure(true);
            builder.hostnameVerifier((hostname, session) -> true).build();
            okHttpClient = builder.build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean judgeChinese() {
//        String isZH = SharedPreUtils.getString(BaseApplication.getContext(), SharedPreUtils.KEY_LANGUAGE);
        return true;
    }

    public static X509TrustManager trustManagerForCertificates = new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
    };

/*    static X509TrustManager trustManagerForCertificates(InputStream in) throws GeneralSecurityException {
        CertificateFactory cerficateFy = CertificateFactory.getInstance("X.509");
        dzInoutCert = cerficateFy.generateCertificates(in);

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : dzInoutCert) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }*/

    static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }


    static InputStream trustedCertificatesInputStream() {

        final String RFINEX = "-----BEGIN CERTIFICATE-----\n" +
                "MIID0DCCArigAwIBAgIQQ1ICP/qokB8Tn+P05cFETjANBgkqhkiG9w0BAQwFADBv\n" +
                "MQswCQYDVQQGEwJTRTEUMBIGA1UEChMLQWRkVHJ1c3QgQUIxJjAkBgNVBAsTHUFk\n" +
                "ZFRydXN0IEV4dGVybmFsIFRUUCBOZXR3b3JrMSIwIAYDVQQDExlBZGRUcnVzdCBF\n" +
                "eHRlcm5hbCBDQSBSb290MB4XDTAwMDUzMDEwNDgzOFoXDTIwMDUzMDEwNDgzOFow\n" +
                "gYUxCzAJBgNVBAYTAkdCMRswGQYDVQQIExJHcmVhdGVyIE1hbmNoZXN0ZXIxEDAO\n" +
                "BgNVBAcTB1NhbGZvcmQxGjAYBgNVBAoTEUNPTU9ETyBDQSBMaW1pdGVkMSswKQYD\n" +
                "VQQDEyJDT01PRE8gRUNDIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MHYwEAYHKoZI\n" +
                "zj0CAQYFK4EEACIDYgAEA0d7L3XJghWF+3XkkRbUq2KZ9T5SCwbOQQB/l+EKJDwd\n" +
                "AQTuPdKNCZcM4HXk+vt3iir1A2BLNosWIxatCXH0SvQoULT+iBxuP2wvLwlZW6Vb\n" +
                "CzOZ4sM9iflqLO+y0wbpo4H+MIH7MB8GA1UdIwQYMBaAFK29mHo0tCb3+sQmVO8D\n" +
                "veAky1QaMB0GA1UdDgQWBBR1cacZSBm8nZ3qQUfflMRId5nTeTAOBgNVHQ8BAf8E\n" +
                "BAMCAYYwDwYDVR0TAQH/BAUwAwEB/zARBgNVHSAECjAIMAYGBFUdIAAwSQYDVR0f\n" +
                "BEIwQDA+oDygOoY4aHR0cDovL2NybC50cnVzdC1wcm92aWRlci5jb20vQWRkVHJ1\n" +
                "c3RFeHRlcm5hbENBUm9vdC5jcmwwOgYIKwYBBQUHAQEELjAsMCoGCCsGAQUFBzAB\n" +
                "hh5odHRwOi8vb2NzcC50cnVzdC1wcm92aWRlci5jb20wDQYJKoZIhvcNAQEMBQAD\n" +
                "ggEBAB3H+i5AtlwFSw+8VTYBWOBTBT1k+6zZpTi4pyE7r5VbvkjI00PUIWxB7Qkt\n" +
                "nHMAcZyuIXN+/46NuY5YkI78jG12yAA6nyCmLX3MF/3NmJYyCRrJZfwE67SaCnjl\n" +
                "lztSjxLCdJcBns/hbWjYk7mcJPuWJ0gBnOqUP3CYQbNzUTcp6PYBerknuCRR2RFo\n" +
                "1KaFpzanpZa6gPim/a5thCCuNXZzQg+HCezF3OeTAyIal+6ailFhp5cmHunudVEI\n" +
                "kAWvL54TnJM/ev/m6+loeYyv4Lb67psSE/5FjNJ80zXrIRKT/mZ1JioVhCb3ZsnL\n" +
                "jbsJQdQYr7GzEPUQyp2aDrV1aug=\n" +
                "-----END CERTIFICATE-----";

        return new Buffer()
                .writeUtf8(RFINEX)
                .inputStream();
    }

}