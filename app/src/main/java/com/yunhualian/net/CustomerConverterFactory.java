package com.yunhualian.net;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by gengda on 2018/4/17.
 */

public final class CustomerConverterFactory extends Converter.Factory {

    public static CustomerConverterFactory create() {
        return create(new Gson());
    }

    public static CustomerConverterFactory create(Gson gson) {
        return new CustomerConverterFactory(gson);
    }

    private final Gson gson;

    private CustomerConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new CustomerRequestBodyConverter<>(gson, type);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CustomerResponseBodyConverter<>(gson, type);
    }

}
