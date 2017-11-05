package com.github.nginate.wolframalpha.retrofit.converter.factory;

import com.github.nginate.wolframalpha.retrofit.converter.ByteArrayConverter;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class ByteArrayConverterFactory extends Converter.Factory {
    private final ByteArrayConverter byteArrayConverter = new ByteArrayConverter();

    private ByteArrayConverterFactory() {
    }

    public static ByteArrayConverterFactory create() {
        return new ByteArrayConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == byte[].class) {
            return byteArrayConverter;
        }
        return null;
    }

}
